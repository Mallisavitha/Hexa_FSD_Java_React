package com.springboot.hospital.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.TestRecommendation;
import com.springboot.hospital.service.TestRecommendationService;

@RestController
@RequestMapping("/api/test")
public class TestRecommendationController {

	@Autowired
	private TestRecommendationService testRecommendationService;

	// Doctor recommends test
	@PostMapping("/recommend/{consultationId}")
	public ResponseEntity<?> recommend(@PathVariable int consultationId, @RequestBody TestRecommendation test) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(testRecommendationService.recommendTest(consultationId, test));
	}

	// view test under a consultation
	@GetMapping("/consultation/{consultationId}")
	public ResponseEntity<?> getByConsultation(@PathVariable int consultationId) {
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.getbyConsultation(consultationId));
	}

	@PutMapping("/update/{testId}")
	public ResponseEntity<?> update(@PathVariable int testId, @RequestBody TestRecommendation updated,Principal principal) {

		String username = principal.getName(); 
		return ResponseEntity.ok(testRecommendationService.updateTest(testId, updated, username));
	}
}
