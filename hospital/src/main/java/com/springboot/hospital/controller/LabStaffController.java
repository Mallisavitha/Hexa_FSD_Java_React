package com.springboot.hospital.controller;

import java.security.Principal;

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

import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.service.LabStaffService;

@RestController
@RequestMapping("/api/labstaff")
public class LabStaffController {
	
	@Autowired
	private LabStaffService labstaffService;
	
	@PostMapping("/add/{departmentId}/{receptionistId}")
	public ResponseEntity<?> addLabStaff(@PathVariable int departmentId,@PathVariable int receptionistId,@RequestBody LabStaff labStaff){
		return ResponseEntity.status(HttpStatus.CREATED).body(labstaffService.addLabStaff(departmentId,receptionistId,labStaff));
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
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id){
		labstaffService.delete(id);
		return ResponseEntity.ok("LabStaff deleted");
	}

}
