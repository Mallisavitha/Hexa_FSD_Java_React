package com.springboot.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.ecom.model.User;
import com.springboot.ecom.repository.UserRepository;


@Service
public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Fetch User by given username
		User user = userRepository.getByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException("Invalid Credentials");

		// Convert your role into Authority as spring works with authority
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole());

		// Add this SimpleGrantedAuthority object into the List now
		List<GrantedAuthority> list = List.of(sga);

		// Convert our user to spring's user that is userdetails
		org.springframework.security.core.userdetails.User springuser = new org.springframework.security.core.userdetails.User(
				user.getUsername(), user.getPassword(), list);

		return springuser;
	}

}

