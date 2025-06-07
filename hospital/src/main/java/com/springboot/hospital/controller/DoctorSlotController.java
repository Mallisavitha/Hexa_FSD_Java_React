package com.springboot.hospital.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.service.DoctorSlotService;

@RestController
@RequestMapping("/api/doctor-slot")
public class DoctorSlotController {
	
	@Autowired
	private DoctorSlotService doctorSlotService;
	
	//add slot by doctor
	@PostMapping("/add")
	public ResponseEntity<?> addSlot(@RequestBody DoctorSlot slot,Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.CREATED).body(doctorSlotService.addSlot(username, slot));
	}
	
	//view own slots of doctor
	@GetMapping("/my-slot")
	public ResponseEntity<?> getOwnSlots(Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.OK).body(doctorSlotService.getSlotsByDoctorUsername(username));
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllSlots(){
		return ResponseEntity.status(HttpStatus.OK).body(doctorSlotService.getAllSlots());
	}

}
