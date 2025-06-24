package com.example.coding.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.coding.repository.PatientRepository;
import com.example.coding.dto.MedicalHistoryDTO;
import com.example.coding.exception.ResourceNotFoundException;
import com.example.coding.model.MedicalHistory;
import com.example.coding.model.Patient;
import com.example.coding.repository.MedicalHistoryRepository;


@Service
public class MedicalHistoryService {
	
	private MedicalHistoryRepository medicalHistoryRepository;
	private PatientRepository patientRepository;
	
	@Autowired
	private MedicalHistoryDTO medicalHistoryDto;

	public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository,
			PatientRepository patientRepository) {
		super();
		this.medicalHistoryRepository = medicalHistoryRepository;
		patientRepository = patientRepository;
	}


//
//	public List<MedicalHistoryDTO> getPatientWithHistory(int patientId) {
//		Patient patient = patientRepository.findById(patientId)
//		        .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
//		
//		List<MedicalHistory> medicalHistories = medicalHistoryRepository.findByPatientId(patientId);
//		
//		List<MedicalHistoryDTO> dtoList = medicalHistories.stream().map(medical -> {
//	        MedicalHistoryDTO dto = new MedicalHistoryDTO();
//	        dto.setIllness(medical.getIllness());
//	        dto.setNumOfYear(medical.getNumOfYear());
//	        dto.setCurrentMedication(medical.getCurrentMedication());
//	        return dto;
//	    }).collect(Collectors.toList());
//
//	    return dtoList;
//	}


}
