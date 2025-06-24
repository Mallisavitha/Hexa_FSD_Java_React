package com.springboot.hospital.controller;

import java.security.Principal;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;
	
	Logger logger=LoggerFactory.getLogger("AppointmentController");

	//Patient book their appointment using slotId
	@PostMapping("/book/{slotId}")
	public ResponseEntity<?> book(@PathVariable int slotId,@RequestBody Appointment appointment,Principal principal){
		String username=principal.getName();
		logger.info("Patient book their appointment for slotId: "+slotId);
		return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(username,slotId,appointment));
	}
	
	//patient can view their own appointment
	@GetMapping("/own")
	public ResponseEntity<?> getMyAppointment(Principal principal){
		String username=principal.getName();
		logger.info("Patient view their own appointments");
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentForPatient(username));
	}
	
	//view appointments assigned to doctor
	@GetMapping("/doctor")
	public ResponseEntity<?> getDoctorAppointment(Principal principal){
		String username=principal.getName();
		logger.info("Doctor is viewing their assigned appointments: "+username);
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentForDoctor(username));
	}
	
	//reschedule the appointment by doctor or receptionist
	@PutMapping("/reschedule/{id}")
	public ResponseEntity<?> reschedule(@PathVariable int id, @RequestBody Appointment updated){
		logger.info("Appointment is being rescheduled");
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.rescheduleAppointment(id, updated));
	}
	
	//get all appointments
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		logger.info("Fetching all appointments in the system");
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllAppointment());
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable int id) {
	    logger.info("Fetching appointment details for ID: " + id);
	    return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getById(id));
	}
	
	@GetMapping("/last/{patientId}")
	public ResponseEntity<?> getLastAppointment(@PathVariable int patientId) {
	    logger.info("Fetching last appointment for patient ID: " + patientId);
	    return ResponseEntity.ok(appointmentService.getLastAppointmentByPatientId(patientId));
	}
	
	@GetMapping("/doctor/last/count")
	public ResponseEntity<?> getAppointmentCountLast7Days(Principal principal){
		String username=principal.getName();
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentCountByDate(username));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable int id) {
	    appointmentService.deleteAppointment(id);
	    return ResponseEntity.ok().body("Appointment deleted successfully with ID: " + id);
	}
}
