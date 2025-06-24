package com.example.coding.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.coding.dto.MedicalHistoryDTO;
import com.example.coding.model.MedicalHistory;
import com.example.coding.repository.MedicalHistoryRepository;


@Service
public class MedicalHistoryService {
	
	private MedicalHistoryRepository medicalHistoryRepository;
	
	@Autowired
	private MedicalHistoryDTO medicalHistoryDto;

	public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository) {
		super();
		this.medicalHistoryRepository = medicalHistoryRepository;
	}

	
	public List<?> getHistoryByPatientId(int patientId) {
		List<MedicalHistory> list=medicalHistoryRepository.findByPatientId(patientId);
		return medicalHistoryDto.convertMedicalHistoryIntoDto(list);
	}

}
