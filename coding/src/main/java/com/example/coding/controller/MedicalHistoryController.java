package com.example.coding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coding.service.MedicalHistoryService;

@RestController
@RequestMapping("api/medical")
public class MedicalHistoryController {
	
	@Autowired
	private MedicalHistoryService medicalHistoryService;

	//get medical history by patint id
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getMedicalByPatientId(@PathVariable int patientId){
		return ResponseEntity.status(HttpStatus.OK).body(medicalHistoryService.getHistoryByPatientId(patientId));
	}
}
