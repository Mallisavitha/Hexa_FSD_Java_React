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
	public void setNumOfYear(int numOfYear) {
		this.numOfYear = numOfYear;
	}
	public String getCurrentMedication() {
		return currentMedication;
	}
	public void setCurrentMedication(String currentMedication) {
		this.currentMedication = currentMedication;
	}
	
	
	
	

}
