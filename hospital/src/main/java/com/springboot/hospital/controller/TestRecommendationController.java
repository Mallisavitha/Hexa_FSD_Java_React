package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.TestRecommendation;
import com.springboot.hospital.service.TestRecommendationService;

@RestController
@RequestMapping("/api/test")
public class TestRecommendationController {

	@Autowired
	private TestRecommendationService testRecommendationService;

	@PostMapping("/add/{consultationId}")
	public ResponseEntity<?> add(@PathVariable int consultationId, @RequestBody TestRecommendation test) {
		return ResponseEntity.status(HttpStatus.CREATED).body(testRecommendationService.addTest(consultationId, test));
	}

	// Assign lab staff to test
	@PutMapping("/assign/{testId}/{labStaffId}")
	public ResponseEntity<?> assign(@PathVariable int testId, @PathVariable int labStaffId) {
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.assignLabStaff(testId, labStaffId));
	}

	//Mark test completed and upload report 
	@PutMapping("/complete/{testId}")
	public ResponseEntity<?> markCompleted(@PathVariable int testId, @RequestParam String reportUrl) {
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.markCompleted(testId, reportUrl));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.getAll());
	}
	
	//get by consultation
	@GetMapping("/consultation/{id}")
	public ResponseEntity<?> getByConsultation(@PathVariable int id){
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.getByConsultation(id));
	}
	
	//get by lab staff
	@GetMapping("/labstaff/{id}")
	public ResponseEntity<?> getByLabStaff(@PathVariable int id){
		return ResponseEntity.status(HttpStatus.OK).body(testRecommendationService.getByLabStaff(id));
	}
	
	@DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        testRecommendationService.delete(id);
        return ResponseEntity.ok("Test deleted");
    }

}
