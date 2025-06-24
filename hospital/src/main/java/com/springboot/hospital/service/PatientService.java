package com.springboot.hospital.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.dto.PatientDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.repository.UserRepository;
import com.springboot.hospital.model.User;

@Service
public class PatientService {

	private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
	private PatientRepository patientRepository;
	private UserService userService;
	private PatientDto patientDto;
	Logger logger = LoggerFactory.getLogger(PatientService.class);

	public PatientService(PasswordEncoder passwordEncoder, UserRepository userRepository,
			PatientRepository patientRepository, UserService userService, PatientDto patientDto) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.userRepository = userRepository;
		this.patientRepository = patientRepository;
		this.userService = userService;
		this.patientDto = patientDto;
	}

	public Patient insertPatient(Patient patient) {
		User user = new User();
	    user.setUsername(patient.getUser().getUsername());
	    user.setPassword(passwordEncoder.encode(patient.getUser().getPassword()));
	    user.setRole("PATIENT");
	    userRepository.save(user);

	    // Set user and department
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

	public List<Patient> getPatientByName(String name) {
		return patientRepository.getPatientByName(name);
	}
	
	public Patient uploadProfilePic(MultipartFile file, String username) throws IOException {
		/* Fetch doctor Info by username */
		Patient patient = patientRepository.getPatientByUsername(username);
		logger.info("This is doctor --> " + patient.getFullName());
		/* extension check: jpg,jpeg,png,gif,svg : */
		String originalFileName = file.getOriginalFilename(); // profile_pic.png
		logger.info(originalFileName.getClass().toString());

		logger.info("" + originalFileName.split("\\.").length);
		String extension = originalFileName.split("\\.")[1]; // png
		if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
			logger.error("extension not approved " + extension);
			throw new RuntimeException("File Extension " + extension + " not allowed " + "Allowed Extensions"
					+ List.of("jpg", "jpeg", "png", "gif", "svg"));
		}
		logger.info("extension approved " + extension);
		/* Check the file size */
		long kbs = file.getSize() / 1024;
		if (kbs > 3000) {
			logger.error("File oversize " + kbs);
			throw new RuntimeException("Image Oversized. Max allowed size is " + kbs);
		}
		logger.info("Profile Image Size " + kbs + " KBs");

		/* Check if Directory exists, else create one */
		String uploadFolder = "D:\\JavaFSD\\nodejs\\react-hospital-ui\\public\\img";
		Files.createDirectories(Path.of(uploadFolder));
		logger.info(Path.of(uploadFolder) + " directory ready!!!");
		/* Define the full path */
		Path path = Paths.get(uploadFolder, "\\", originalFileName);
		/* Upload file in the above path */
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		/* Set url of file or image in doctor object */
		patient.setProfilePic(originalFileName);
		/* Save doctor Object */
		return patientRepository.save(patient);
	}

	public Patient uploadProfilePicById(MultipartFile file, int patientId) throws IOException {
		Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

		/* extension check: jpg,jpeg,png,gif,svg : */
		String originalFileName = file.getOriginalFilename(); // profile_pic.png
		logger.info(originalFileName.getClass().toString());

		logger.info("" + originalFileName.split("\\.").length);
		String extension = originalFileName.split("\\.")[1]; // png
		if (!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
			logger.error("extension not approved " + extension);
			throw new RuntimeException("File Extension " + extension + " not allowed " + "Allowed Extensions"
					+ List.of("jpg", "jpeg", "png", "gif", "svg"));
		}
		logger.info("extension approved " + extension);
		/* Check the file size */
		long kbs = file.getSize() / 1024;
		if (kbs > 3000) {
			logger.error("File oversize " + kbs);
			throw new RuntimeException("Image Oversized. Max allowed size is " + kbs);
		}
		logger.info("Profile Image Size " + kbs + " KBs");

		/* Check if Directory exists, else create one */
		String uploadFolder = "D:\\JavaFSD\\nodejs\\react-hospital-ui\\public\\img";
		Files.createDirectories(Path.of(uploadFolder));
		logger.info(Path.of(uploadFolder) + " directory ready!!!");
		/* Define the full path */
		Path path = Paths.get(uploadFolder, "\\", originalFileName);
		/* Upload file in the above path */
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		/* Set url of file or image in doctor object */
		patient.setProfilePic(originalFileName);
		/* Save doctor Object */
		return patientRepository.save(patient);
	}

	public Patient updatePatientById(int id, Patient updatedPatient) {
		// Fetch existing doctor
		Patient existing = patientRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

		// Update fields
		existing.setFullName(updatedPatient.getFullName());
		existing.setDob(updatedPatient.getDob());
		existing.setGender(updatedPatient.getGender());
		existing.setContactNumber(updatedPatient.getContactNumber());

		return patientRepository.save(existing);
	}

}
