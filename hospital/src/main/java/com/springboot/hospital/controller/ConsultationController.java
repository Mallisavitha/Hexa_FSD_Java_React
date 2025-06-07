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

import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.service.ConsultationService;

@RestController
@RequestMapping("/api/consultation")
public class ConsultationController {

	@Autowired
	private ConsultationService consultationService;

	// doctor add consultation for appointment
	@PostMapping("/add/{appointmentId}")
	public ResponseEntity<?> add(@PathVariable int appointmentId, @RequestBody Consultation consultation) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(consultationService.addConsultation(appointmentId, consultation));
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.getAll());
	}

	// get consultation by appointment ID for doctor
	@GetMapping("/doctor/get/{appointmentId}")
	public ResponseEntity<?> getConsultationForDoctor(@PathVariable int appointmentId,
	                                                  Principal principal) {
	    String username = principal.getName();
	    return ResponseEntity.ok(consultationService.getForDoctorByAppointmentId(appointmentId, username));
	}

	// update consultatio by ID
	@PutMapping("/update/{appointmentId}")
	public ResponseEntity<?> updateConsulattion(@PathVariable int appointmentId, @RequestBody Consultation updated) {
		return ResponseEntity.status(HttpStatus.OK).body(consultationService.updateConsultation(appointmentId, updated));
	}

	//get consulation for patient
	@GetMapping("/patient/get/{appointmentId}")
	public ResponseEntity<?> getConsultationForPatient(@PathVariable int appointmentId, Principal principal) {
		String username = principal.getName();
		return ResponseEntity.ok(consultationService.getForPatientByAppointmentId(appointmentId, username));
	}

}
