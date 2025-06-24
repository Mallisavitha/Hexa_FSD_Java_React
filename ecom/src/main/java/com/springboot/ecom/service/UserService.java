package com.springboot.ecom.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.ecom.model.User;
import com.springboot.ecom.model.Customer;
import com.springboot.ecom.repository.UserRepository;
import com.springboot.ecom.repository.CustomerRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    public User signUp(User user) {
		//encrypt the pain text password given
		String plainPassword=user.getPassword();
		String encodePassword=passwordEncoder.encode(plainPassword);
		user.setPassword(encodePassword);
		
		//Save User in DB
		return userRepository.save(user);
	}

    public Object getUserInfo(String username) {
        User user = userRepository.findByUsername(username);
         

        switch (user.getRole().toUpperCase()) {
            case "CUSTOMER":
                Customer customer = customerRepository.getCustomerByUsername(username);      
                return customer;
            case "EXECUTIVE":
                // return executive info when implemented
                return null;
            default:
                return null;
        }
    }
}
