package com.springboot.hospital.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Appointment;


@Component
public class AppointmentDto {
	
	private int appointmentId;
	private LocalDate scheduledDate;
	private LocalTime scheduledTime;
	private String natureOfVisit;
	private String description;
	private String status;
	
	private String patientName;
	private String contactNumber;
	private String doctorName;
	private String specialization;
	
	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public int getAppointmentId() {
		return appointmentId;
	}
	
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	public LocalDate getScheduledDate() {
		return scheduledDate;
	}
	
	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	
	public LocalTime getScheduledTime() {
		return scheduledTime;
	}
	
	public void setScheduledTime(LocalTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	
	public String getNatureOfVisit() {
		return natureOfVisit;
	}
	
	public void setNatureOfVisit(String natureOfVisit) {
		this.natureOfVisit = natureOfVisit;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
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
	
	public List<AppointmentDto> convertAppointmentIntoDto(List<Appointment> list){
		List<AppointmentDto> listDto=new ArrayList<>();
		list.stream().forEach(appointment -> {
			AppointmentDto dto=new AppointmentDto();
			dto.setAppointmentId(appointment.getAppointmentId());
			dto.setScheduledDate(appointment.getScheduledDate());
			dto.setScheduledTime(appointment.getScheduledTime());
			dto.setNatureOfVisit(appointment.getNatureOfVisit());
			dto.setDescription(appointment.getDescription());
			dto.setStatus(appointment.getStatus().name());
			dto.setPatientName(appointment.getPatient().getFullName());
			dto.setDoctorName(appointment.getDoctor().getFullName());
			dto.setContactNumber(appointment.getPatient().getContactNumber());
			dto.setSpecialization(appointment.getDoctor().getSpecialization());
			listDto.add(dto);
		});
		return listDto;
	}
	
	

}
