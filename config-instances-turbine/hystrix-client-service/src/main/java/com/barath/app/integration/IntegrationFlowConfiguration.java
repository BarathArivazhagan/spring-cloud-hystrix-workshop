package com.barath.app.integration;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.integration.annotation.Poller;
import com.barath.app.model.Customer;

@Configuration
public class IntegrationFlowConfiguration {
	
	private static final Logger logger =LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final RestTemplate restTemplate = new RestTemplate();
	private static final String CUSTOMERS_ENDPOINT = "/customers";
	private final Environment environment;
	private String url;
	
	@Value("${server.port}")
	private int port;
	
	
	
	
	public IntegrationFlowConfiguration(Environment environment) {
		super();
		this.environment = environment;
		
	}
	


	@Bean
	@InboundChannelAdapter(value = "inputChannel", poller = @Poller(fixedDelay = "1000"))
    public Supplier<List<Customer>> invokeCustomerCommands() throws RestClientException, URISyntaxException {
        
		if(logger.isInfoEnabled()) {
			logger.info("inbound channel invoked");
		}
		
         return () -> {
        	 ResponseEntity<List<Customer>> responseEntity= restTemplate.exchange(this.url,HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){});
        	 
        	 return responseEntity.getBody();
         };
		
    }
	
	@Bean
	public MessageChannel inputChannel() {
		return new DirectChannel();
	}
	
	
	@Bean
	public IntegrationFlow inboundFlow(HandlerService handlerService) {
		
		return IntegrationFlows.from(inputChannel())
				.handle(handlerService)
					.get();
	}
	
	@Component
	protected static class HandlerService implements MessageHandler{

		

		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			
			if(logger.isInfoEnabled()){
				logger.info("Message recevied {}",message.toString());
			}
			
		}
		
		
		
	}
	
	private String setUrl() {
		
		boolean isCloud = Arrays.asList(environment.getActiveProfiles()).contains("cloud");
		String hostname = null;
		
		if(isCloud) {
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {			
				e.printStackTrace();
			}		
			this.url = "https://".concat(hostname).concat(CUSTOMERS_ENDPOINT);
		}else {
			String port = environment.getProperty("server.port");
			this.url = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_ENDPOINT);
		}
		
		if(logger.isInfoEnabled()) {
			logger.info(" url {}",this.url);
		}
		return url;
	}
	
	
	
	@PostConstruct
	public void init() {
		this.setUrl();
		
	}

}
