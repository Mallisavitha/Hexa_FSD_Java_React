package com.example.coding.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.coding.model.Doctor;
import com.example.coding.model.Patient;
import com.example.coding.model.User;
import com.example.coding.repository.DoctorRepository;
import com.example.coding.repository.PatientRepository;
import com.example.coding.repository.UserRepository;


@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			PatientRepository patientRepository, DoctorRepository doctorRepository) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
	}

	public User signUp(User user) {
		// encrypt the pain text password given
		String plainPassword = user.getPassword();
		String encodePassword = passwordEncoder.encode(plainPassword);
		user.setPassword(encodePassword);

		// Save User in DB
		return userRepository.save(user);
	}

	public Object getUserInfo(String username) {
		User user = userRepository.findByUsername(username);
		switch (user.getRole().toUpperCase()) {
		case "PATIENT":
			Patient patient = patientRepository.getPatientByUsername(username);
			return patient;

		case "DOCTOR":
			Doctor doctor = doctorRepository.getDoctorByUsername(username);
			return doctor;

		default:
			throw new RuntimeException("Unknown role: " + user.getRole());

		}

	}

}
