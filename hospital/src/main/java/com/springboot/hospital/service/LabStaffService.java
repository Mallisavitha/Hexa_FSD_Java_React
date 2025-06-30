package com.springboot.hospital.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.dto.LabStaffDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.ReceptionistRepository;

@Service
public class LabStaffService {

	private PasswordEncoder passwordEncoder;
	private LabStaffRepository labRepository;
	private DepartmentRepository departmentRepository;
	private ReceptionistRepository receptionistRepository;
	private UserService userService;
	private LabStaffDto labStaffdto;
	
	Logger logger = LoggerFactory.getLogger(LabStaffService.class);

	public LabStaffService(PasswordEncoder passwordEncoder, LabStaffRepository labRepository,
			DepartmentRepository departmentRepository, ReceptionistRepository receptionistRepository,
			UserService userService, LabStaffDto labStaffdto) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.labRepository = labRepository;
		this.departmentRepository = departmentRepository;
		this.receptionistRepository = receptionistRepository;
		this.userService = userService;
		this.labStaffdto = labStaffdto;
	}

	public LabStaff addLabStaff(int departmentId, LabStaff labStaff, String receptionistUsername) {
		User user = labStaff.getUser();
		user.setRole("LABSTAFF");
		user = userService.signUp(user);
		labStaff.setUser(user);

		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid Department ID"));
		Receptionist receptionist = receptionistRepository.getReceptionistByUsername(receptionistUsername);

		labStaff.setDepartment(department);
		labStaff.setReceptionist(receptionist);

		return labRepository.save(labStaff);
	}

	public List<LabStaffDto> getAll() {
		List<LabStaff> list=labRepository.findAll();
		return labStaffdto.convertLabIntoDto(list);
	}

	public LabStaff getById(int id) {
		return labRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lab Staff not found"));
	}

	public void delete(String username) {
		LabStaff lab=getLabstaffByUsername(username);
		labRepository.delete(lab);
	}

	public LabStaff getLabstaffByUsername(String username) {
		return labRepository.getLabstaffByUsername(username);
	}
	
	 public LabStaff updateLabStaff(String username, LabStaff updated) {
	        LabStaff labStaff = getLabstaffByUsername(username);

	        if (updated.getName() != null) labStaff.setName(updated.getName());
	        if (updated.getEmail() != null) labStaff.setEmail(updated.getEmail());
	        if (updated.getContact() != null) labStaff.setContact(updated.getContact());

	        return labRepository.save(labStaff);
	    }
	 
	 public LabStaff uploadProfilePic(MultipartFile file, String username) throws IOException {
			/* Fetch doctor Info by username */
			LabStaff labstaff = labRepository.getLabstaffByUsername(username);
			logger.info("This is LabStaff --> " + labstaff.getName());
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
			labstaff.setProfilePic(originalFileName);
			/* Save doctor Object */
			return labRepository.save(labstaff);
		}
	 
	 public LabStaff uploadProfilePicById(MultipartFile file, int labStaffId) throws IOException {
			LabStaff labstaff = labRepository.findById(labStaffId).orElseThrow(() -> new RuntimeException("Doctor not found"));

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
			labstaff.setProfilePic(originalFileName);
			/* Save doctor Object */
			return labRepository.save(labstaff);
		}
	 
		public LabStaff updateLabStaffById(int id, LabStaff updatedStaff) {
			// Fetch existing doctor
			LabStaff existing = labRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));

			// Update fields
			existing.setName(updatedStaff.getName());
			existing.setEmail(updatedStaff.getEmail());
			

			return labRepository.save(existing);
		}


}
