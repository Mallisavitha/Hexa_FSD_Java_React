package com.springboot.hospital.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.service.DoctorSlotService;

@RestController
@RequestMapping("/api/doctor-slot")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorSlotController {

	@Autowired
	private DoctorSlotService doctorSlotService;

	Logger logger = LoggerFactory.getLogger("DoctorSlotController");

	// add slot by doctor
	@PostMapping("/add")
	public ResponseEntity<?> addSlot(@RequestBody DoctorSlot slot, Principal principal) {
		String username = principal.getName();
		logger.info("Doctor adding the slot");
		return ResponseEntity.status(HttpStatus.CREATED).body(doctorSlotService.addSlot(username, slot));
	}

	// view own slots of doctor
	@GetMapping("/my-slot")
	public ResponseEntity<?> getOwnSlots(Principal principal) {
		String username = principal.getName();
		logger.info("Doctor can view their own slots");
		return ResponseEntity.status(HttpStatus.OK).body(doctorSlotService.getSlotsByDoctorUsername(username));
	}

	@GetMapping("/all")
	public ResponseEntity<?> getAllSlots() {
		logger.info("View all slots");
		return ResponseEntity.status(HttpStatus.OK).body(doctorSlotService.getAllSlots());
	}

	@GetMapping("/doctor-name/{name}")
	public ResponseEntity<?> getSlotsByDoctorName(@PathVariable String name) {
		return ResponseEntity.status(HttpStatus.OK).body(doctorSlotService.getSlotByDoctorName(name));
	}

	@DeleteMapping("/delete/{slotId}")
	public ResponseEntity<?> deleteSlot(@PathVariable int slotId, Principal principal) {
		String username = principal.getName();
		doctorSlotService.deleteSlotByDoctor(slotId, username);
		return ResponseEntity.ok("Slot deleted successfully");
	}

	@GetMapping("/by-doctor/{doctorId}")
	public ResponseEntity<?> getAvailableSlots(@PathVariable int doctorId) {
		List<DoctorSlot> slots = doctorSlotService.getAvailableSlots(doctorId);
		return ResponseEntity.ok(slots);
	}
}
