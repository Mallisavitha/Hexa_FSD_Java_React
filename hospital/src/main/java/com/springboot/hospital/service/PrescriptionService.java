package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.Prescription;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.PrescriptionRepository;

@Service
public class PrescriptionService {
	
	private PrescriptionRepository prescriptionRepository;
	private ConsultationRepository consultationRepository;
	
	public PrescriptionService(PrescriptionRepository prescriptionRepository,
			ConsultationRepository consultationRepository) {
		this.prescriptionRepository = prescriptionRepository;
		this.consultationRepository = consultationRepository;
	}

	public Prescription addPrescription(int consultationId, Prescription prescription) {
		Consultation consultation=consultationRepository.findById(consultationId).orElseThrow(() -> new ResourceNotFoundException("Invalid Consultation ID"));
		prescription.setConsultation(consultation);
		return prescriptionRepository.save(prescription);
	}

	public List<Prescription> getAll() {
		return prescriptionRepository.findAll();
	}

	public Prescription getById(int id) {
		return prescriptionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
	}

	public List<Prescription> getByConsultation(int consultationId) {
		return prescriptionRepository.getByConsultationId(consultationId);
	}

}
