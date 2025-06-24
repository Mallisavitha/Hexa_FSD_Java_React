package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Doctor;

@Component
public class DoctorDto {
	
	private int doctorId;
	private String fullName;
	private String specialization;
	private int experienceYear;
	private String qualification;
	private String designation;
	
	
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public int getExperienceYear() {
		return experienceYear;
	}
	public void setExperienceYear(int experienceYear) {
		this.experienceYear = experienceYear;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public List<DoctorDto> convertDoctorIntoDto(List<Doctor> list){
		List<DoctorDto> listDto=new ArrayList<>();
		list.stream().forEach(doctor ->{
			DoctorDto dto=new DoctorDto();
			dto.setDoctorId(doctor.getDoctorId());
			dto.setFullName(doctor.getFullName());
			dto.setSpecialization(doctor.getSpecialization());
			dto.setExperienceYear(doctor.getExperienceYear());
			dto.setQualification(doctor.getQualification());
			dto.setSpecialization(doctor.getSpecialization());
			dto.setDesignation(doctor.getDesignation());
			listDto.add(dto);
		});
		return listDto;
	}

}
