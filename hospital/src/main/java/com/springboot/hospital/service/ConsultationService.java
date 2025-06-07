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
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid appointment ID"));
		// Check if consultation already exists
		consultationRepository.findByAppointmentId(appointmentId).ifPresent(c -> {
			throw new RuntimeException("Consultation already exists for this appointment");
		});
		consultation.setAppointment(appointment);
		return consultationRepository.save(consultation);
	}

	public List<Consultation> getAll() {
		return consultationRepository.findAll();
	}

	public Consultation getForDoctorByAppointmentId(int appointmentId, String doctorUsername) {
		Consultation consultation = consultationRepository.findByAppointmentId(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

		return consultation;
	}

	public Consultation updateConsultation(int appointmentId, Consultation updated) {
		Consultation consultation = consultationRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));
		if (updated.getSymptoms() != null)
			consultation.setSymptoms(updated.getSymptoms());

		if (updated.getExamination() != null)
			consultation.setExamination(updated.getExamination());

		if (updated.getTreatmentPlan() != null)
			consultation.setTreatmentPlan(updated.getTreatmentPlan());

		return consultationRepository.save(consultation);
	}

	public Consultation getForPatientByAppointmentId(int appointmentId, String patientUsername) {
		Consultation consultation = consultationRepository.findByAppointmentId(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

		return consultation;
	}

}
