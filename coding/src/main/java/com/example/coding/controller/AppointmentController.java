package com.example.coding.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coding.model.Appointment;
import com.example.coding.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/book/{slotId}")
	public ResponseEntity<?> bookAppointment(@PathVariable int slotId, @RequestBody Appointment appointment,
			Principal principal) {
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(username,slotId,appointment));

	}
}
