package com.springboot.hospital.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.ConsultationDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.PatientRepository;

@Service
public class ConsultationService {

	private ConsultationRepository consultationRepository;
	private AppointmentRepository appointmentRepository;
	private ConsultationDto consultationDto;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	
	Logger logger=LoggerFactory.getLogger(ConsultationService.class);

	public ConsultationService(ConsultationRepository consultationRepository,
			AppointmentRepository appointmentRepository, ConsultationDto consultationDto,
			PatientRepository patientRepository, DoctorRepository doctorRepository) {
		super();
		this.consultationRepository = consultationRepository;
		this.appointmentRepository = appointmentRepository;
		this.consultationDto = consultationDto;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
	}

	public Consultation addConsultation(int appointmentId, Consultation consultation) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid appointment ID"));
		// Check if consultation already exists
		consultationRepository.findByAppointmentId(appointmentId).ifPresent(c -> {
			throw new RuntimeException("Consultation already exists for this appointment");
		});
		consultation.setAppointment(appointment);
		logger.info("Consultation saved successfully for appointmentId:{} ",appointmentId);
		return consultationRepository.save(consultation);
	}

	public List<ConsultationDto> getAll() {
		logger.info("Fetching all consultations");
		List<Consultation> list = consultationRepository.findAll();
		return consultationDto.convertConsultationIntoDto(list);
	}

	public ConsultationDto getForDoctorByAppointmentId(int appointmentId, String doctorUsername) {
		Consultation consultation = consultationRepository.findByAppointmentId(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

		Doctor doctor = doctorRepository.getDoctorByUsername(doctorUsername);

		// Check if the doctor is the one associated with this consultation
		if (consultation.getAppointment().getDoctor().getDoctorId() != doctor.getDoctorId()) {
			throw new ResourceNotFoundException("Doctor not authorized to view this consultation");
		}

		logger.info("Consultation fetched successfully for doctor {} ",doctorUsername);
		return consultationDto.convertConsultationIntoDto(List.of(consultation)).get(0);
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

		logger.info("Consultation updated successfully for appointmentId: {}" ,appointmentId);
		return consultationRepository.save(consultation);
	}

	public ConsultationDto getForPatientByAppointmentId(int appointmentId, String patientUsername) {
		
		Consultation consultation = consultationRepository.findByAppointmentId(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

		Patient patient = patientRepository.getPatientByUsername(patientUsername);

		// Check if the patient is the one associated with this consultation
		if (consultation.getAppointment().getPatient().getPatientId() != patient.getPatientId()) {
			throw new ResourceNotFoundException("Patient not authorized to view this consultation");
		}

		logger.info("Consultation fetched successfully for patient:{} ",patientUsername);
		return consultationDto.convertConsultationIntoDto(List.of(consultation)).get(0);
	}

}
