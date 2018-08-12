package com.barath.app.controller;

import com.barath.app.model.Customer;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class CustomerController {

    @GetMapping("/customers")
    @HystrixCommand(commandKey = "getCustomers")
    public List<Customer> customers(){

        return Arrays.asList(new Customer(1L,"barath"),new Customer(2L,"barath"));

    }


}
