package com.barath.app.integration;

import java.lang.invoke.MethodHandles;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
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
import org.springframework.scheduling.annotation.Scheduled;
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
	private static final String CUSTOMERS_WITH_THREAD20_ENDPOINT = "/customersWithThreadPool20";
	private static final String CUSTOMERS_WITH_THREAD30_ENDPOINT = "/customersWithThreadPool30";
	private static final String CUSTOMERS_WITH_THREAD40_ENDPOINT = "/customersWithThreadPool40";
	private static final String CUSTOMERS_WITH_THREAD50_ENDPOINT = "/customersWithThreadPool50";
	private final Environment environment;
	private String[] urls = new String[5];
	
	
	
	public IntegrationFlowConfiguration(Environment environment) {
		super();
		this.environment = environment;
		
	}
	


	@Bean	
	@InboundChannelAdapter(value = "inputChannel", poller = @Poller(fixedDelay = "1000"))
    public Supplier<String> invokeCustomerCommands() throws RestClientException, URISyntaxException {
        
		if(logger.isInfoEnabled()) {
			logger.info("inbound channel invoked");
		}
		
         return () -> {
        	 CompletableFuture.supplyAsync( () -> restTemplate.exchange(this.urls[0],HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){}));
        	 CompletableFuture.supplyAsync( () -> restTemplate.exchange(this.urls[1],HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){}));
        	 CompletableFuture.supplyAsync( () ->  restTemplate.exchange(this.urls[2],HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){}));
        	 CompletableFuture.supplyAsync( () -> restTemplate.exchange(this.urls[3],HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){}));
        	 CompletableFuture.supplyAsync( () ->  restTemplate.exchange(this.urls[4],HttpMethod.GET,null,new ParameterizedTypeReference<List<Customer>>(){}));
        	 return "execute";
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
	
	private void setUrls() {
		
		boolean isCloud = Arrays.asList(environment.getActiveProfiles()).contains("cloud");
		String hostname = null;
		
		if(isCloud) {
			try {
				hostname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {			
				e.printStackTrace();
			}		
			this.urls[0] = "https://".concat(hostname).concat(CUSTOMERS_ENDPOINT);
			this.urls[1] = "https://".concat(hostname).concat(CUSTOMERS_WITH_THREAD20_ENDPOINT);
			this.urls[2] = "https://".concat(hostname).concat(CUSTOMERS_WITH_THREAD30_ENDPOINT);
			this.urls[3] = "https://".concat(hostname).concat(CUSTOMERS_WITH_THREAD40_ENDPOINT);
			this.urls[4] = "https://".concat(hostname).concat(CUSTOMERS_WITH_THREAD50_ENDPOINT);
		}else {
			String port = environment.getProperty("server.port");
			this.urls[0] = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_ENDPOINT);
			this.urls[1] = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_WITH_THREAD20_ENDPOINT);
			this.urls[2] = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_WITH_THREAD30_ENDPOINT);
			this.urls[3] = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_WITH_THREAD40_ENDPOINT);
			this.urls[4] = "http://".concat("localhost:").concat(port).concat(CUSTOMERS_WITH_THREAD50_ENDPOINT);
		}
		
		if(logger.isInfoEnabled()) {
			logger.info(" url {}",this.urls.toString());
		}
		
	}
	
	
	
	@PostConstruct
	public void init() {
		this.setUrls();
		
	}

}
