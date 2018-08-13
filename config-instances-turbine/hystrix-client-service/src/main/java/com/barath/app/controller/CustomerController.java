package com.barath.app.controller;

import com.barath.app.model.Customer;
import com.barath.app.service.CustomerService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/customersWithThreadPool20")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool20",threadPoolKey= "thread20")
    public List<Customer> customersWithThreadPool20(){

        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool30")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool",threadPoolKey= "thread30")
    public List<Customer> customersWithThreadPool30(){

        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool40")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool40",threadPoolKey= "thread40")
    public List<Customer> customersWithThreadPool40(){

        return customerService.getCustomers();

    }
	
	@GetMapping("/customersWithThreadPool50")
    @HystrixCommand(commandKey = "getCustomersWithThreadPool50",threadPoolKey= "thread50")
    public List<Customer> customersWithThreadPool(){

        return customerService.getCustomers();

    }


}
