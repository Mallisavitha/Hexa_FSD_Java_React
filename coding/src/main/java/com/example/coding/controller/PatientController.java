package com.example.coding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coding.model.Patient;
import com.example.coding.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addPatient(@RequestBody Patient patient){
		return ResponseEntity.status(HttpStatus.CREATED).body(patientService.addPatientWithMedicalHistory(patient));
	}
	
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<?> getPatientByDoctorId(@PathVariable int doctorId){
		return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientByDoctorId(doctorId));
	}
}
