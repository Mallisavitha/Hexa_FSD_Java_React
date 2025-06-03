package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.ConsultationRepository;

@Service
public class ConsultationService {
	
	private ConsultationRepository consultationRepository;
	private AppointmentRepository appointmentRepository;

	public ConsultationService(ConsultationRepository consultationRepository,
			AppointmentRepository appointmentRepository) {
		this.consultationRepository = consultationRepository;
		this.appointmentRepository = appointmentRepository;
	}

	public Consultation addConsultation(int appointmentId, Consultation consultation) {
		Appointment appointment=appointmentRepository.findById(appointmentId).orElseThrow(() -> new ResourceNotFoundException("Invalid appointment ID"));
		consultation.setAppointment(appointment);
		return consultationRepository.save(consultation);
	}

	public List<Consultation> getAll() {
		return consultationRepository.findAll();
	}

	public Consultation getById(int id) {
		return consultationRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Invalid Consultation Id"));
	}

	public List<Consultation> getByPatient(int patientId) {
		return consultationRepository.getByPatientId(patientId);
	}

	public List<Consultation> getByDoctor(int doctorId) {
		return consultationRepository.getByDoctorId(doctorId);
	}

	public void delete(int id) {
		getById(id);
		consultationRepository.deleteById(id);
	}


}
