package com.springboot.hospital.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User signUp(User user) {
		//encrypt the pain text password given
		String plainPassword=user.getPassword();
		String encodePassword=passwordEncoder.encode(plainPassword);
		user.setPassword(encodePassword);
		
		//Save User in DB
		return userRepository.save(user);
	}

}
