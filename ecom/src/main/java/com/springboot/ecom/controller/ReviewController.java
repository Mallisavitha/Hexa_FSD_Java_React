package com.springboot.ecom.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.ecom.model.Review;
import com.springboot.ecom.service.ReviewService;

@RestController
@RequestMapping("api/review")
public class ReviewController {
	
	@Autowired
	private ReviewService reviewService;

	@PostMapping("/add/{productId}")
	public ResponseEntity<?> addReview(@PathVariable int productId,@RequestBody Review review,Principal principal){
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.addReview(principal.getName(),productId,review));
	}
	
	@GetMapping("/product/{productId}")
	public ResponseEntity<?> getReview(@PathVariable int productId){
		return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewByProduct(productId));
	}
}
