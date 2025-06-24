package com.springboot.hospital.controller;

import java.security.Principal;

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

import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.service.LabStaffService;

@RestController
@RequestMapping("/api/labstaff")
public class LabStaffController {
	
	@Autowired
	private LabStaffService labstaffService;
	
	Logger logger=LoggerFactory.getLogger("LabStaffController");
	
	@PostMapping("/add/{departmentId}")
	public ResponseEntity<?> addLabStaff(@PathVariable int departmentId,Principal principal,@RequestBody LabStaff labStaff){
		logger.info("Receptionist add labstaff to department");
		return ResponseEntity.status(HttpStatus.CREATED).body(labstaffService.addLabStaff(departmentId,labStaff,principal.getName()));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		logger.info("Get all labStaff details");
		return ResponseEntity.status(HttpStatus.OK).body(labstaffService.getAll());
	}
	
	@GetMapping("/get-one")
	public LabStaff getLabstaffById(Principal principal){
		String username=principal.getName();
		logger.info("Labstaff view their own profile");
		return labstaffService.getLabstaffByUsername(username);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody LabStaff updated) {
		String username=principal.getName();
		logger.info("Update the labStaff details");
		return ResponseEntity.ok(labstaffService.updateLabStaff(username, updated));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(Principal principal){
		String username=principal.getName();
		labstaffService.delete(username);
		return ResponseEntity.ok("LabStaff deleted");
	}

}
