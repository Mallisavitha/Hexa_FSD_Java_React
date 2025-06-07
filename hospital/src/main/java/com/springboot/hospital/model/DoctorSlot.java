package com.springboot.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="doctor_slot")
public class DoctorSlot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int slotId;
	
	private LocalDate date;
	private LocalTime time;
	private int maxAppointment;
	private int bookedCount=0;

	@ManyToOne
	private Doctor doctor;

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public int getMaxAppointment() {
		return maxAppointment;
	}

	public void setMaxAppointment(int maxAppointment) {
		this.maxAppointment = maxAppointment;
	}

	public int getBookedCount() {
		return bookedCount;
	}

	public void setBookedCount(int bookedCount) {
		this.bookedCount = bookedCount;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	
	
}
