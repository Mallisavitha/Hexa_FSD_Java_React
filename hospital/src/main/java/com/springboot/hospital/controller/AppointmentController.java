package com.springboot.hospital.controller;

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

import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/add/{patientId}/{doctorId}/{receptionistId}")
	public ResponseEntity<?> insert(@PathVariable int patientId, @PathVariable int doctorId,
			@PathVariable int receptionistId, @RequestBody Appointment appointment) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(appointmentService.insertAppointment(patientId, doctorId, receptionistId, appointment));
	}

	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getById(@PathVariable int id) {
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAppointmentById(id));
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getAllAppointment());
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		appointmentService.deleteAppointment(id);
		return ResponseEntity.ok("Appointment deleted");
	}

	// reschedule appointment
	@PutMapping("/reschedule/{id}")
	public ResponseEntity<?> reschedule(@PathVariable int id, @RequestBody Appointment updated) {
		return ResponseEntity.status(HttpStatus.OK).body(
				appointmentService.rescheduleAppointment(id, updated.getScheduledDate(), updated.getScheduledTime()));
	}

	// get appoitment by patientid
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getByPatient(@PathVariable int patientId) {
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByPatient(patientId));
	}

	// get appoitment by doctorid
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<?> getByDoctor(@PathVariable int doctorId) {
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByDoctor(doctorId));
	}

	// get appoitment by doctorid
	@GetMapping("/receptionist/{adminId}")
	public ResponseEntity<?> getByReceptionist(@PathVariable int adminId) {
		return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByReceptionist(adminId));
	}
	
	 @GetMapping("/status/{status}")
	    public ResponseEntity<?> getByStatus(@PathVariable Appointment.Status status) {
	        return ResponseEntity.status(HttpStatus.OK).body(appointmentService.getByStatus(status));
	    }
}
