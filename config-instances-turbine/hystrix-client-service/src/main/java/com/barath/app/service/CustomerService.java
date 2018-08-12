package com.barath.app.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.barath.app.model.Customer;

@Service
public class CustomerService {
	
	public List<Customer> getCustomers(){
		
		return Arrays.asList(new Customer(1L,"barath"),new Customer(2L,"barath"));
	}

}
