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

	public List<Prescription> getByConsultation(int consultationId) {
		return prescriptionRepository.getByConsultationId(consultationId);
	}

	public Prescription updatePrescription(int consultationId, Prescription updated) {
		Prescription prescription=prescriptionRepository.findById(consultationId).orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
		if(updated.getMedicineName() != null)
			prescription.setMedicineName(updated.getMedicineName());
		
		if(updated.getDosageTiming() != null)
			prescription.setDosageTiming(updated.getDosageTiming());
		
		if(updated.getDuration() != null)
			prescription.setDuration(updated.getDuration());
		
		if(updated.getMealTime() != null)
			prescription.setMealTime(updated.getMealTime());
		
		return prescriptionRepository.save(prescription);
	}

	public void deletePrescription(int prescriptionId) {
		Prescription prescription=prescriptionRepository.findById(prescriptionId).orElseThrow(() -> new ResourceNotFoundException("Prescription not found"));
		prescriptionRepository.delete(prescription);
	}

}
