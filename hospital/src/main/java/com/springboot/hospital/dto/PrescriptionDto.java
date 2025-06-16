package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Prescription;

@Component
public class PrescriptionDto {

	private int prescriptionId;
	private String medicineName;
	private String dosageTiming;
	private String duration;
	private String mealTime;
	private int consultationId;
	private String patientName;
	private String doctorName;
	
	public int getPrescriptionId() {
		return prescriptionId;
	}
	
	public void setPrescriptionId(int prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	
	public String getMedicineName() {
		return medicineName;
	}
	
	public void setMedicineName(String medicineName) {
		this.medicineName = medicineName;
	}
	
	public String getDosageTiming() {
		return dosageTiming;
	}
	
	public void setDosageTiming(String dosageTiming) {
		this.dosageTiming = dosageTiming;
	}
	
	public String getDuration() {
		return duration;
	}
	
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getMealTime() {
		return mealTime;
	}
	
	public void setMealTime(String mealTime) {
		this.mealTime = mealTime;
	}
	
	public int getConsultationId() {
		return consultationId;
	}
	
	public void setConsultationId(int consultationId) {
		this.consultationId = consultationId;
	}
	
	public String getPatientName() {
		return patientName;
	}
	
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public List<PrescriptionDto> convertPrescriptionIntoDto(List<Prescription> list){
		List<PrescriptionDto> listDto=new ArrayList<>();
		list.stream().forEach(prescription -> {
			PrescriptionDto dto=new PrescriptionDto();
			dto.setPrescriptionId(prescription.getPrescriptionId());
			dto.setMedicineName(prescription.getMedicineName());
			dto.setDosageTiming(prescription.getDosageTiming());
			dto.setDuration(prescription.getDuration());
			dto.setMealTime(prescription.getMealTime().name());
			dto.setConsultationId(prescription.getConsultation().getConsultationId());
			dto.setPatientName(prescription.getConsultation().getAppointment().getPatient().getFullName());
			dto.setDoctorName(prescription.getConsultation().getAppointment().getDoctor().getFullName());
			listDto.add(dto);
			
		});
		return listDto;
	}
	
	
}
