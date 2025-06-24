package com.springboot.ecom.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecom.service.CustomerProductService;

@RestController
@RequestMapping("/api/purchase")
public class CustomerProductController {

	@Autowired
	private CustomerProductService customerProductService;

	@PostMapping("/{productId}")
	public ResponseEntity<?> purchaseProduct(@PathVariable int productId, Principal principal) {
		return ResponseEntity.status(HttpStatus.OK).body(customerProductService.purchaseProduct(principal.getName(), productId));
	}
}
