package com.springboot.hospital.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.repository.ReceptionistRepository;
import com.springboot.hospital.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private ReceptionistRepository receptionistRepository;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	private LabStaffRepository labStaffRepository;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			ReceptionistRepository receptionistRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository, LabStaffRepository labStaffRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.receptionistRepository = receptionistRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.labStaffRepository = labStaffRepository;
	}


	public User signUp(User user) {
		//encrypt the pain text password given
		String plainPassword=user.getPassword();
		String encodePassword=passwordEncoder.encode(plainPassword);
		user.setPassword(encodePassword);
		
		//Save User in DB
		return userRepository.save(user);
	}
	
	public Object getUserInfo(String username) {
		User user= userRepository.findByUsername(username);
		switch(user.getRole().toUpperCase()) {
		case "PATIENT":
			Patient patient=patientRepository.getPatientByUsername(username);
			return patient;
			
		case "DOCTOR":
			Doctor doctor=doctorRepository.getDoctorByUsername(username);
			return doctor;
			
		case "RECEPTIONIST":
			Receptionist receptionist=receptionistRepository.getReceptionistByUsername(username);
			return receptionist;
			
		case "LABSTAFF":
			LabStaff labstaff=labStaffRepository.getLabstaffByUsername(username);
			return labstaff;
			
		default:
			throw new RuntimeException("Unknown role: "+user.getRole());
		
		}
		
	}



}
