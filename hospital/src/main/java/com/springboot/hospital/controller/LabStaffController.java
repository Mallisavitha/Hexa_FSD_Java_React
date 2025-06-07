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

import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.service.LabStaffService;

@RestController
@RequestMapping("/api/labstaff")
public class LabStaffController {
	
	@Autowired
	private LabStaffService labstaffService;
	
	@PostMapping("/add/{departmentId}")
	public ResponseEntity<?> addLabStaff(@PathVariable int departmentId,Principal principal,@RequestBody LabStaff labStaff){
		return ResponseEntity.status(HttpStatus.CREATED).body(labstaffService.addLabStaff(departmentId,labStaff,principal.getName()));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(labstaffService.getAll());
	}
	
	@GetMapping("/get-one")
	public LabStaff getLabstaffById(Principal principal){
		String username=principal.getName();
		return labstaffService.getLabstaffByUsername(username);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody LabStaff updated) {
		String username=principal.getName();
		return ResponseEntity.ok(labstaffService.updateLabStaff(username, updated));
	}
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(Principal principal){
		String username=principal.getName();
		labstaffService.delete(username);
		return ResponseEntity.ok("LabStaff deleted");
	}

}
