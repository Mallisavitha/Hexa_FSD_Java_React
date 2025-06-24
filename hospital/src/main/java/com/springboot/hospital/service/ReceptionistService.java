package com.springboot.hospital.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.dto.ReceptionistDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.ReceptionistRepository;

@Service
public class ReceptionistService {

	private ReceptionistRepository recepRepository;
	private UserService userService;
	private ReceptionistDto receptionistDto;
	Logger logger = LoggerFactory.getLogger(ReceptionistService.class);

	public ReceptionistService(ReceptionistRepository recepRepository, UserService userService,
			ReceptionistDto receptionistDto) {
		super();
		this.recepRepository = recepRepository;
		this.userService = userService;
		this.receptionistDto = receptionistDto;
	}

	public Receptionist insertReceptionist(Receptionist receptionist) {
		User user = receptionist.getUser();
		user.setRole("RECEPTIONIST");
		user=userService.signUp(user);
		receptionist.setUser(user);
		return recepRepository.save(receptionist);
	}

	public List<ReceptionistDto> getAll() {
		List<Receptionist> list=recepRepository.findAll();
		return receptionistDto.convertReceptionistIntoDto(list);
	}

	public Receptionist getById(int id) {
		return recepRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid Id"));
	}

	public Receptionist update(String username, Receptionist updated) {
		Receptionist db = getReceptionistByUsername(username);
		if (updated.getName() != null)
			db.setName(updated.getName());
		if (updated.getEmail() != null)
			db.setEmail(updated.getEmail());
		if (updated.getPhone() != null)
			db.setPhone(updated.getPhone());
		return recepRepository.save(db);
	}

	public void delete(String username) {
		Receptionist receptionist=getReceptionistByUsername(username);
		recepRepository.delete(receptionist);
	}

	public Receptionist getReceptionistByUsername(String username) {
		return recepRepository.getReceptionistByUsername(username);
	}
	
	public Receptionist uploadProfilePic(MultipartFile file, String username) throws IOException {
		/* Fetch doctor Info by username */
		Receptionist recep = recepRepository.getReceptionistByUsername(username);
		logger.info("This is doctor --> " + recep.getName());
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
		recep.setProfilePic(originalFileName);
		/* Save doctor Object */
		return recepRepository.save(recep);
	}

	public Receptionist uploadProfilePicById(MultipartFile file, int adminId) throws IOException {
		Receptionist recep = recepRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Doctor not found"));

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
		recep.setProfilePic(originalFileName);
		/* Save doctor Object */
		return recepRepository.save(recep);
	}

}
