package com.springboot.hospital.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.PatientDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.model.User;

@Service
public class PatientService {

	private PatientRepository patientRepository;
	private UserService userService;
	private PatientDto patientDto;

	public PatientService(PatientRepository patientRepository, UserService userService, PatientDto patientDto) {
		super();
		this.patientRepository = patientRepository;
		this.userService = userService;
		this.patientDto = patientDto;
	}

	public Patient insertPatient(Patient patient) {
		// Take user our of this patient object
		User user = patient.getUser();

		// Give role to this user
		user.setRole("PATIENT");

		// Save this User in the DB
		user = userService.signUp(user);

		// Attach this user back to learner
		patient.setUser(user);

		// Save learner in DB
		return patientRepository.save(patient);
	}

	public List<PatientDto> getAllPatient() {
		List<Patient> list=patientRepository.findAll();
		return patientDto.convertPatientIntoDto(list);
	}

	public Patient getPatientById(int id) {
		// TODO Auto-generated method stub
		return patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient ID Not Found " + id));
	}

	public Patient updatePatient(String username, Patient updatedPatient) {
		Patient patient=getPatientByUsername(username);
		if (updatedPatient.getFullName() != null)
			patient.setFullName(updatedPatient.getFullName());
		if (updatedPatient.getDob() != null)
			patient.setDob(updatedPatient.getDob());
		if (updatedPatient.getGender() != null)
			patient.setGender(updatedPatient.getGender());
		if (updatedPatient.getContactNumber() != null)
			patient.setContactNumber(updatedPatient.getContactNumber());

		return patientRepository.save(patient);

	}

	public void deletePatient(String username) {
	    Patient patient = getPatientByUsername(username);
	    patientRepository.delete(patient);
	}


	public Patient getPatientByUsername(String username) {
		return patientRepository.getPatientByUsername(username);
	}

	public List<PatientDto> getPatientByDoctorSpecialization(String specialization) {
		List<Patient> list=patientRepository.getPatientsBySpecialization(specialization);
		return patientDto.convertPatientIntoDto(list);
	}

	public List<PatientDto> getPatientsByAppointment(LocalDate parseDate) {
		List<Patient> list=patientRepository.getPatientByAppointmentDate(parseDate);
		return patientDto.convertPatientIntoDto(list);
	}

}
