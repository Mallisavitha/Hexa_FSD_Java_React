package com.springboot.hospital.controller;

import java.security.Principal;

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

import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	@PostMapping("/add/{deptId}")
	public ResponseEntity<?> insertDoctor(@PathVariable int deptId, Principal principal,@RequestBody Doctor doctor) {
		return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.insertDoctor(deptId, doctor, principal.getName()));
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(doctorService.getAllDoctors());
	}

	@GetMapping("/get-one")
	public Doctor getDoctorById(Principal principal) {
		String username = principal.getName();
		return doctorService.getDoctorByUsername(username);
	}

	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody Doctor updatedDoctor) {
		String username=principal.getName();
		return ResponseEntity.ok(doctorService.updateDoctor(username, updatedDoctor));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(Principal principal) {
		String username=principal.getName();
		doctorService.deleteDoctor(username);
		return ResponseEntity.ok("Doctor deleted");
	}

	@GetMapping("/specialization/{specialization}")
	public ResponseEntity<?> getBySpecialization(@PathVariable String specialization) {
		return ResponseEntity.ok(doctorService.getBySpecialization(specialization));
	}
	
	@GetMapping("/search-name/{name}")
	public ResponseEntity<?> searchByName(@PathVariable String name){
		return ResponseEntity.ok(doctorService.searchByName(name));
	}
}
