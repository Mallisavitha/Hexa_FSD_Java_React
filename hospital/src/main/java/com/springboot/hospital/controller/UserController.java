package com.springboot.hospital.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.User;
import com.springboot.hospital.service.UserService;
import com.springboot.hospital.util.JwtUtil;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/signup")
	public User SignUp(@RequestBody User user) {
		return userService.signUp(user);
	}

	@GetMapping("/token")
	public ResponseEntity<?> getToken(Principal principal) {
		try {
		String token =jwtUtil.createToken(principal.getName()); 
		Map<String, Object> map = new HashMap<>();
		map.put("token", token);
		return ResponseEntity.status(HttpStatus.OK).body(map);
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
	
	@GetMapping("/details")
	public Object getLoggedInUserDetails(Principal principal) {
		String username = principal.getName(); // loggedIn username
		/**
		 * Lets get the Role info of this User
		 * As we dont know who the user really is? Learner? Author?
		 */
		Object object = userService.getUserInfo(username);
		return object;
	}
}
