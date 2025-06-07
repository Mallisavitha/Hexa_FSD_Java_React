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

import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	//Patient book their appointment using slotId
	@PostMapping("/book/{slotId}")
	public ResponseEntity<?> book(@PathVariable int slotId,@RequestBody Appointment appointment,Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(username,slotId,appointment));
	}
	
	//patient can view their own appointment
	@GetMapping("/own")
	public ResponseEntity<?> getMyAppointment(Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentForPatient(username));
	}
	
	//view appointments assigned to doctor
	@GetMapping("/doctor")
	public ResponseEntity<?> getDoctorAppointment(Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentForDoctor(username));
	}
	
	//reschedule the appointment by doctor or receptionist
	@PutMapping("/reschedule/{id}")
	public ResponseEntity<?> reschedule(@PathVariable int id, @RequestBody Appointment updated){
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.rescheduleAppointment(id, updated));
	}
	
	//get all appointments
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllAppointment());
	}
}
