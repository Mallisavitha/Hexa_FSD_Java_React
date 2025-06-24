package com.springboot.hospital.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.service.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

	@Autowired
	private PatientService patientService;
	
	Logger logger=LoggerFactory.getLogger("PatientController");

	// POST: ADD Patient
	@PostMapping("/add")
	public ResponseEntity<?> insertPatient(@RequestBody Patient patient) {
		logger.info("Patient is adding");
		return ResponseEntity.status(HttpStatus.CREATED).body(patientService.insertPatient(patient));

	}

	// GET: ALL Patients
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		logger.info("Receptionist adding all patients details");
		return ResponseEntity.ok(patientService.getAllPatient());
	}

	// GET: GetPatient by ID
	@GetMapping("/get-one")
	public Patient getPatientById(Principal principal) {

		//Ask spring username of loggedIn user using Principal interface
		String username = principal.getName();
		logger.info("Patient viewing their own details");
		return patientService.getPatientByUsername(username);
	}

	// PUT:Update Patient by using id
	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody Patient updatedPatient) {
		String username=principal.getName();
		logger.info("Patient updating their own details");
		return ResponseEntity.ok(patientService.updatePatient(username, updatedPatient));
	}

	// DELETE: delete patient by id
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePatient(Principal principal) {
	    String username = principal.getName();
	    patientService.deletePatient(username);
	    return ResponseEntity.ok("Patient profile deleted successfully");
	}
	
	//GET: get patient by using specialization
	@GetMapping("/specialization/{specialization}")
	public ResponseEntity<?> getPatientsByDoctorSpecialization(@PathVariable String specialization){
		return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientByDoctorSpecialization(specialization));
	}
	
	//GET : get patient by appointmentdate
	@GetMapping("/date/{date}")
	public ResponseEntity<?> getByDate(@PathVariable String date){
		LocalDate parseDate=LocalDate.parse(date);
		return ResponseEntity.status(HttpStatus.OK).body(patientService.getPatientsByAppointment(parseDate));
	}

}
