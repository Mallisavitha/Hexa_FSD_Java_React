package com.springboot.hospital.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Patient;

@Component
public class PatientDto {
	
	private int patientId;
	private String fullName;
	private LocalDate dob;
	private String gender;
	private String contactNumber;
	
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	
	
	public List<PatientDto> convertPatientIntoDto(List<Patient> list){
		List<PatientDto> listDto=new ArrayList<>();
		list.stream().forEach(patient -> {
			PatientDto dto=new PatientDto();
			dto.setPatientId(patient.getPatientId());
			dto.setFullName(patient.getFullName());
			dto.setDob(patient.getDob());
			dto.setGender(patient.getGender());
			dto.setContactNumber(patient.getContactNumber());
			listDto.add(dto);
		});
		return listDto;
	}
	
	
	

}
