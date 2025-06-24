package com.springboot.ecom.service;

import org.springframework.stereotype.Service;

import com.springboot.ecom.model.Customer;
import com.springboot.ecom.repository.CustomerRepository;
import com.springboot.ecom.model.User;

@Service
public class CustomerService {

	private CustomerRepository customerRepository;
	private UserService userService;

	public CustomerService(CustomerRepository customerRepository, UserService userService) {
		super();
		this.customerRepository = customerRepository;
		this.userService = userService;
	}

	public Customer insertCustomer(Customer customer) {
		// Take user our of this learner object
		User user = customer.getUser();

		// Give role to this user
		user.setRole("CUSTOMER");

		// Save this User in the DB
		user = userService.signUp(user);

		// Attach this user back to learner
		customer.setUser(user);

		// Save learner in DB
		return customerRepository.save(customer);
	}

}
