package com.barath.app.controller;

import com.barath.app.model.Customer;
import com.barath.app.service.CustomerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
public class CustomerController {
	
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private final CustomerService customerService;
	

    public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}



	@GetMapping("/customers")
    @HystrixCommand(commandKey = "getCustomers")
    public List<Customer> customers(){
		
		if(logger.isInfoEnabled()) logger.info("customers invoked");
        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool20")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool20",threadPoolKey= "thread20")
    public List<Customer> customersWithThreadPool20(){
		
		if(logger.isInfoEnabled()) logger.info("customersWithThreadPool20 invoked");
        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool30")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool",threadPoolKey= "thread30")
    public List<Customer> customersWithThreadPool30(){
		
		if(logger.isInfoEnabled()) logger.info("customersWithThreadPool30 invoked");
        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool40")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool40",threadPoolKey= "thread40")
    public List<Customer> customersWithThreadPool40(){
		
		if(logger.isInfoEnabled()) logger.info("customersWithThreadPool40 invoked");
        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool50")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool50",threadPoolKey= "thread50")
    public List<Customer> customersWithThreadPool(){
		
		if(logger.isInfoEnabled()) logger.info("customersWithThreadPool50 invoked");
        return customerService.getCustomers();

    }


}
