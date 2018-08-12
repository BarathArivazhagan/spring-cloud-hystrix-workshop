package com.barath.app.controller;

import com.barath.app.model.Customer;
import com.barath.app.service.CustomerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CustomerController {
	
	private final CustomerService customerService;
	

    public CustomerController(CustomerService customerService) {
		super();
		this.customerService = customerService;
	}



	@GetMapping("/customers")
    @HystrixCommand(commandKey = "getCustomers")
    public List<Customer> customers(){

        return customerService.getCustomers();

    }


}
