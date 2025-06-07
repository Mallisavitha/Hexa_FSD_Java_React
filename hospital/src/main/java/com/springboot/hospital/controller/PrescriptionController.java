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

	// get prescription by consultation Id
	@GetMapping("/get/{consultationId}")
	public ResponseEntity<?> getByConsultation(@PathVariable int consultationId){
		return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.getByConsultation(consultationId));
	}

	//update Prescription
	@PutMapping("update/{consultationId}")
	public ResponseEntity<?> updatePrescription(@PathVariable int consultationId, @RequestBody Prescription updated){
		return ResponseEntity.status(HttpStatus.OK).body(prescriptionService.updatePrescription(consultationId, updated));
	}
	
	//Delete Prescription by ID
	@DeleteMapping("/delete/{prescriptionId}")
	public ResponseEntity<?> delete(@PathVariable int prescriptionId){
		prescriptionService.deletePrescription(prescriptionId);
        return ResponseEntity.ok("Prescription deleted");
	}
}
