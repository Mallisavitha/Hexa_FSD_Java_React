package com.springboot.hospital.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.dto.AppointmentDto;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	Logger logger = LoggerFactory.getLogger("AppointmentController");

	// Patient book their appointment using slotId
	@PostMapping("/book/{doctorId}")
	public ResponseEntity<?> book(@PathVariable int doctorId, @RequestBody AppointmentDto dto, Principal principal) {
		String username = principal.getName();
		logger.info("Patient book their appointment for slotId: " + doctorId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(appointmentService.bookAppointment(username, doctorId, dto));
	}

	// patient can view their own appointment
	@GetMapping("/own/upcoming")
	public ResponseEntity<?> getUpcomingAppointmentsForPatient(Principal principal) {

		String username = principal.getName();
		logger.info("Patient views their upcoming/today appointments");

		return ResponseEntity.status(HttpStatus.OK)
				.body(appointmentService.getUpcomingAppointmentsForPatient(username));
	}

	@GetMapping("/own/past")
	public ResponseEntity<?> getPastAppointmentsForPatient(Principal principal,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {

		String username = principal.getName();
		logger.info("Patient views their past appointments");

		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getPastAppointmentsForPatient(username, page, size));
	}

	// reschedule the appointment by doctor or receptionist
	@PutMapping("/reschedule/{id}")
	public ResponseEntity<?> reschedule(@PathVariable int id, @RequestBody Appointment updated) {
		logger.info("Appointment is being rescheduled");
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.rescheduleAppointment(id, updated));
	}

	// get all appointments
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		logger.info("Fetching all appointments in the system");
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllAppointment());
	}

	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable int id) {
		logger.info("Fetching appointment details for ID: " + id);
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getById(id));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteAppointment(@PathVariable int id) {
		appointmentService.deleteAppointment(id);
		return ResponseEntity.ok().body("Appointment deleted successfully with ID: " + id);
	}

	@GetMapping("/last-7-days")
	public List<Map<String, Object>> getLast7DaysAppointments() {
		return appointmentService.getLast7DaysAppointments();
	}

	@GetMapping("/own/date")
	public ResponseEntity<?> searchAppointmentsByDate(@RequestParam String date, Principal principal) {
		String username = principal.getName();
		LocalDate localDate = LocalDate.parse(date); // Format: yyyy-MM-dd
		return ResponseEntity.ok(appointmentService.getAppointmentsByDate(username, localDate));
	}

	@GetMapping("/doctor/new")
	public ResponseEntity<?> getTodayAppointments(Principal principal) {
		String username = principal.getName();
		return ResponseEntity.ok(appointmentService.getTodayAppointmentsForDoctor(username));
	}

	@GetMapping("/doctor/upcoming")
	public ResponseEntity<?> getUpcomingAppointments(Principal principal) {
		String username = principal.getName();
		return ResponseEntity.ok(appointmentService.getUpcomingAppointmentsForDoctor(username));
	}

	@GetMapping("/doctor/past")
	public ResponseEntity<?> getPastAppointments(Principal principal) {
		String username = principal.getName();
		return ResponseEntity.ok(appointmentService.getPastAppointmentsForDoctor(username));
	}

}
