package com.springboot.hospital.service;

import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.DoctorDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.ReceptionistRepository;
import com.springboot.hospital.repository.UserRepository;

@Service
public class DoctorService {

	private PasswordEncoder passwordEncoder;
	private DoctorRepository doctorRepository;
	private DepartmentRepository deptRepository;
	private ReceptionistRepository receptionistRepository;
	private UserRepository userRepository;
	private UserService userService;
	private DoctorDto doctorDto;
	Logger logger = LoggerFactory.getLogger(DoctorService.class);

	public DoctorService(PasswordEncoder passwordEncoder, DoctorRepository doctorRepository,
			DepartmentRepository deptRepository, ReceptionistRepository receptionistRepository,
			UserRepository userRepository, UserService userService, DoctorDto doctorDto) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.doctorRepository = doctorRepository;
		this.deptRepository = deptRepository;
		this.receptionistRepository = receptionistRepository;
		this.userRepository = userRepository;
		this.userService = userService;
		this.doctorDto = doctorDto;
	}

	public Doctor insertDoctor(int deptId, Doctor doctor, String addedBy) {
	    Department dept = deptRepository.findById(deptId)
	            .orElseThrow(() -> new RuntimeException("Department not found with ID: " + deptId));
	 // Create new user and associate
	    User user = new User();
	    user.setUsername(doctor.getUser().getUsername());
	    user.setPassword(passwordEncoder.encode(doctor.getUser().getPassword()));
	    user.setRole("DOCTOR");
	    userRepository.save(user);

	    // Set user and department
	    doctor.setUser(user);
	    doctor.setDepartment(dept);
	    doctor.setAddedBy(addedBy); // if you're tracking this
	    return doctorRepository.save(doctor);
	}

	public List<?> getAllDoctors(int page, int size) {
		// Activate Pageable interface
		Pageable pageable = PageRequest.of(page, size);
		List<Doctor> list = doctorRepository.findAll(pageable).getContent();
		return doctorDto.convertDoctorIntoDto(list);
	}

	public Doctor getDoctorById(int id) {
		return doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Doctor ID: " + id));
	}

	public Doctor updateDoctor(String username, Doctor updatedDoctor) {
		Doctor dbDoctor = getDoctorByUsername(username);
		if (updatedDoctor.getFullName() != null)
			dbDoctor.setFullName(updatedDoctor.getFullName());
		if (updatedDoctor.getSpecialization() != null)
			dbDoctor.setSpecialization(updatedDoctor.getSpecialization());
		if (updatedDoctor.getExperienceYear() != 0)
			dbDoctor.setExperienceYear(updatedDoctor.getExperienceYear());
		if (updatedDoctor.getQualification() != null)
			dbDoctor.setQualification(updatedDoctor.getQualification());
		if (updatedDoctor.getDesignation() != null)
			dbDoctor.setDesignation(updatedDoctor.getDesignation());
		if (updatedDoctor.getContact() != null)
			dbDoctor.setContact(updatedDoctor.getContact());
		return doctorRepository.save(dbDoctor);
	}

	public void deleteDoctor(String username) {
		Doctor doctor = getDoctorByUsername(username);
		doctorRepository.delete(doctor);
	}

	public List<DoctorDto> getBySpecialization(String specialization) {
		List<Doctor> list = doctorRepository.getBySpecialization(specialization);
		return doctorDto.convertDoctorIntoDto(list);
	}

	public Doctor getDoctorByUsername(String username) {
		return doctorRepository.getDoctorByUsername(username);
	}

	public List<DoctorDto> searchByName(String name) {
		List<Doctor> list = doctorRepository.searchByName(name);
		return doctorDto.convertDoctorIntoDto(list);
	}

	public Doctor uploadProfilePic(MultipartFile file, String username) throws IOException {
		/* Fetch doctor Info by username */
		Doctor doctor = doctorRepository.getDoctorByUsername(username);
		logger.info("This is doctor --> " + doctor.getFullName());
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
		doctor.setProfilePic(originalFileName);
		/* Save doctor Object */
		return doctorRepository.save(doctor);
	}

	public Doctor updateDoctorById(int id, Doctor updatedDoctor) {
		// Fetch existing doctor
		Doctor existing = doctorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

		// Update fields
		existing.setFullName(updatedDoctor.getFullName());
		existing.setSpecialization(updatedDoctor.getSpecialization());
		existing.setExperienceYear(updatedDoctor.getExperienceYear());
		existing.setQualification(updatedDoctor.getQualification());
		existing.setDesignation(updatedDoctor.getDesignation());
		existing.setContact(updatedDoctor.getContact());

		return doctorRepository.save(existing);
	}

	public Doctor uploadProfilePicById(MultipartFile file, int doctorId) throws IOException {
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

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
		doctor.setProfilePic(originalFileName);
		/* Save doctor Object */
		return doctorRepository.save(doctor);
	}

	public void deleteDoctorById(int id) {
		Optional<Doctor> doctor=doctorRepository.findById(id);
		doctorRepository.deleteById(id);
		
	}

}
