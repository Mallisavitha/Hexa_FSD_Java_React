package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.service.ConsultationService;

@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {
	
	@Autowired
	private ConsultationService consultationService;

	@PostMapping("/add/{appointmentId}")
	public ResponseEntity<?> add(@PathVariable int appointmentId,@RequestBody Consultation consultation){
		return ResponseEntity.status(HttpStatus.CREATED).body(consultationService.addConsultation(appointmentId, consultation));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.getAll());
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable int id){
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.getById(id));
	}
	
	//get consultation by patientId
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getByPatient(@PathVariable int patientId){
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.getByPatient(patientId));
	}
	
	//get consultation by doctorId
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<?> getByDoctor(@PathVariable int doctorId){
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.getByDoctor(doctorId));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id){
		consultationService.delete(id);
		return ResponseEntity.ok("Consultation deleted");
	}
}
