package com.example.coding.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.coding.model.MedicalHistory;

@Component
public class MedicalHistoryDTO {
	
	private String illness;
	private int numOfYear;
	private String currentMedication;
	
	public String getIllness() {
		return illness;
	}
	public void setIllness(String illness) {
		this.illness = illness;
	}
	public int getNumOfYear() {
		return numOfYear;
	}
	public void setNumOfYears(int numOfYears) {
		this.numOfYear = numOfYears;
	}
	public String getCurrentMedication() {
		return currentMedication;
	}
	public void setCurrentMedication(String currentMedication) {
		this.currentMedication = currentMedication;
	}
	
	public List<MedicalHistoryDTO> convertMedicalHistoryIntoDto(List<MedicalHistory> list) {
		List<MedicalHistoryDTO> dtoList=new ArrayList<>();
		for(MedicalHistory history : list) {
			MedicalHistoryDTO dto=new MedicalHistoryDTO();
			dto.setIllness(history.getIllness());
			dto.setNumOfYears(history.getNumOfYear());
			dto.setCurrentMedication(history.getCurrentMedication());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	
	

}
