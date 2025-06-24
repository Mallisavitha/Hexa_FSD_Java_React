package com.example.coding.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.example.coding.model.Doctor;
import com.example.coding.model.MedicalHistory;
import com.example.coding.model.Patient;
import com.example.coding.model.User;
import com.example.coding.repository.MedicalHistoryRepository;
import com.example.coding.repository.PatientRepository;

import jakarta.transaction.Transactional;

@Service
public class PatientService {

	private PatientRepository patientRepository;
	private MedicalHistoryRepository medicalHistoryRepository;
	private UserService userService;

	public PatientService(PatientRepository patientRepository, MedicalHistoryRepository medicalHistoryRepository,
			UserService userService) {
		super();
		this.patientRepository = patientRepository;
		this.medicalHistoryRepository = medicalHistoryRepository;
		this.userService = userService;
	}

	@Transactional
	public Patient addPatientWithMedicalHistory(Patient patient) {
		// Take user our of this patient object
		User user = patient.getUser();
		// Give role to this user
		user.setRole("PATIENT");
		// Save this User in the DB
		user = userService.signUp(user);
		// Attach this user back to learner
		patient.setUser(user);
		
		//save patient
		Patient save=patientRepository.save(patient);
		
		//attach patient to each history by batch save
		List<MedicalHistory> history=patient.getMedicalHistories();
		for(MedicalHistory histories : history) {
			histories.setPatient(patient);
		}
		medicalHistoryRepository.saveAll(history);
		
		return save;
	}

	public List<Patient> getPatientByDoctorId(int doctorId) {
		return patientRepository.findPatientsByDoctorId(doctorId);
	}
	
	
	public List<Patient> getPatientBySpeciality(Doctor.Speciality speciality){
		return patientRepository.getPatientByDoctorSpeciality(speciality);
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
