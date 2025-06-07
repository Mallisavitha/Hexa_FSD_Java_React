package com.springboot.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="appointment")
public class Appointment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int appointmentId;
	
	@Column(name="scheduled_date")
	private LocalDate scheduledDate;
	
	@Column(name="scheduled_time")
	private LocalTime scheduledTime;
	
	@Column(name="nature_of_visit")
	private String natureOfVisit;
	
	@Column(name="description")
	private String description;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public enum Status{
		SCHEDULED,COMPLETED,RESCHEDULED
	}
	
	@ManyToOne
	private Doctor doctor;
	
	@ManyToOne
	private Receptionist receptionist;

	@ManyToOne
	private Patient patient;
	
	@ManyToOne
	private DoctorSlot doctorSlot;
	
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Receptionist getReceptionist() {
		return receptionist;
	}

	public void setReceptionist(Receptionist receptionist) {
		this.receptionist = receptionist;
	}

	public DoctorSlot getDoctorSlot() {
		return doctorSlot;
	}

	public void setDoctorSlot(DoctorSlot doctorSlot) {
		this.doctorSlot = doctorSlot;
	}
	
	

}
