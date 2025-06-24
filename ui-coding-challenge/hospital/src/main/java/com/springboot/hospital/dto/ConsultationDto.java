package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Consultation;

@Component
public class ConsultationDto {
	
	private int consultationId;
	private String symptoms;
	private String examination;
	private String treatmentPlan;
	private int appointmentId;
	private String doctorName;
	private String patientName;
	
	public int getConsultationId() {
		return consultationId;
	}
	
	public void setConsultationId(int consultationId) {
		this.consultationId = consultationId;
	}
	
	public String getSymptoms() {
		return symptoms;
	}
	
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}
	
	public String getExamination() {
		return examination;
	}
	
	public void setExamination(String examination) {
		this.examination = examination;
	}
	
	public String getTreatmentPlan() {
		return treatmentPlan;
	}
	
	public void setTreatmentPlan(String treatmentPlan) {
		this.treatmentPlan = treatmentPlan;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public List<ConsultationDto> convertConsultationIntoDto(List<Consultation> list){
		List<ConsultationDto> listDto=new ArrayList<>();
		list.stream().forEach(consultation -> {
			ConsultationDto dto=new ConsultationDto();
			dto.setConsultationId(consultation.getConsultationId());
			dto.setSymptoms(consultation.getSymptoms());
			dto.setExamination(consultation.getExamination());
			dto.setTreatmentPlan(consultation.getTreatmentPlan());
			dto.setAppointmentId(consultation.getAppointment().getAppointmentId());
			dto.setPatientName(consultation.getAppointment().getPatient().getFullName());
			dto.setDoctorName(consultation.getAppointment().getDoctor().getFullName());
			listDto.add(dto);
		});
		return listDto;
	}
	
	
	

}
