package com.springboot.hospital.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.repository.ReceptionistRepository;


@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepository;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	private ReceptionistRepository receptionistRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository, ReceptionistRepository receptionistRepository) {
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.receptionistRepository = receptionistRepository;
	}

	public Appointment insertAppointment(int patientId, int doctorId, int receptionistId, Appointment appointment) {

		// Step 1: Validate all foreign keys using findById
		Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new ResourceNotFoundException("Invalid patient ID"));
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new ResourceNotFoundException("Invalid doctor ID"));
		Receptionist receptionist = receptionistRepository.findById(receptionistId).orElseThrow(() -> new ResourceNotFoundException("Invalid receptionist ID"));

		// Step 2: Set the fetched FK objects into appointment
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);
		appointment.setReceptionist(receptionist);

		// Step 3: Save appointment
		return appointmentRepository.save(appointment);
	}

	public Appointment getAppointmentById(int id) {
		return appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment Id not found"));
	}

	public List<Appointment> getAllAppointment() {
		return appointmentRepository.findAll();
	}

	public void deleteAppointment(int id) {
        getAppointmentById(id); // validation
        appointmentRepository.deleteById(id);
    }

	 public Appointment rescheduleAppointment(int id, LocalDate scheduledDate, LocalTime scheduledTime) {
	        Appointment appt = getAppointmentById(id);
	        appt.setScheduledDate(scheduledDate);
	        appt.setScheduledTime(scheduledTime);
	        appt.setStatus(Appointment.Status.SCHEDULED);
	        return appointmentRepository.save(appt);
	    }

	public List<Appointment> getByPatient(int patientId) {
		return appointmentRepository.getByPatientId(patientId);
	}
	
	public List<Appointment> getByDoctor(int doctorId) {
		return appointmentRepository.getByPatientId(doctorId);
	}
	
	public List<Appointment> getByReceptionist(int adminId) {
		return appointmentRepository.getByPatientId(adminId);
	}
	
	public List<Appointment> getByStatus(Appointment.Status status) {
        return appointmentRepository.findByStatus(status);
    }

}
