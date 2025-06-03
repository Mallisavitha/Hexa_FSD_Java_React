package com.springboot.hospital.model;

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
@Table(name="prescription")
public class Prescription {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int prescriptionId;
	
	@Column(name="medicine_name")
	private String medicineName;
	
	@Column(name="dosage_timing")
	private String dosageTiming;
	
	@Column(name="meal_time")
	@Enumerated(EnumType.STRING)
	private MealTime mealTime;
	
	public enum MealTime{
		BF,AF
	}
	
	@ManyToOne
	private Consultation consultation;

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

	public MealTime getMealTime() {
		return mealTime;
	}

	public void setMealTime(MealTime mealTime) {
		this.mealTime = mealTime;
	}

	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
	}
	
	

}
