package com.springboot.hospital.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin(origins = "http://localhost:5173")
public class TestRecommendationController {

	@Autowired
	private TestRecommendationService testRecommendationService;

	Logger logger = LoggerFactory.getLogger("TestRecommendationController");

	// Doctor recommends test
	@PostMapping("/recommend/{consultationId}")
	public ResponseEntity<?> recommend(@PathVariable int consultationId, @RequestBody TestRecommendation test) {
		logger.info("Doctor recommending test for consultationId: "+consultationId);
		return ResponseEntity.status(HttpStatus.CREATED).body(testRecommendationService.recommendTest(consultationId, test));
	}

	// view test under a consultation
	@GetMapping("/consultation/{consultationId}")
	public ResponseEntity<?> getByConsultation(@PathVariable int consultationId) {
		logger.info("Fetching test recommendations for consultation ID");
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.getbyConsultation(consultationId));
	}

	@PutMapping("/update/{testId}")
	public ResponseEntity<?> update(@PathVariable int testId, @RequestBody TestRecommendation updated,
			Principal principal) {

		String username = principal.getName();
		logger.info("LabStaff updating test ID: "+testId);
		return ResponseEntity.ok(testRecommendationService.updateTest(testId, updated, username));
	}
}
