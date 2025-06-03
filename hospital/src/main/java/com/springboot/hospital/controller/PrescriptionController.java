package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.Prescription;
import com.springboot.hospital.service.PrescriptionService;

@RestController
@RequestMapping("/api/prescription")
public class PrescriptionController {
	
	@Autowired
	private  PrescriptionService prescriptionService;
	
	@PostMapping("/add/{consultationId}")
	public ResponseEntity<?> add(@PathVariable int consultationId,@RequestBody Prescription prescription){
		return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionService.addPrescription(consultationId, prescription));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.getAll());
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable int id){
		return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.getById(id));
	}
	
	// get prescription by consultation Id
	@GetMapping("/consultation/{consultationId}")
	public ResponseEntity<?> getByConsultation(@PathVariable int consultationId){
		return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.getByConsultation(consultationId));
	}

}
