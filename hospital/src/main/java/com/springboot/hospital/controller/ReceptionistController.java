package com.springboot.hospital.controller;

import java.io.IOException;
import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.hospital.model.Patient;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.service.ReceptionistService;

@RestController
@RequestMapping("/api/receptionist")
@CrossOrigin(origins = "http://localhost:5173")
public class ReceptionistController {

	@Autowired
	private ReceptionistService receptionistService;
	
	Logger logger=LoggerFactory.getLogger("ReceptionistController");

	@PostMapping("/add")
	public ResponseEntity<?> insert(@RequestBody Receptionist receptionist) {
		logger.info("New Receptionist registered");
		return ResponseEntity.status(HttpStatus.CREATED).body(receptionistService.insertReceptionist(receptionist));
	}

	@GetMapping("/get-all")
	public ResponseEntity<?> getAll() {
		logger.info("Fetching all receptionist record");
		return ResponseEntity.status(HttpStatus.OK).body(receptionistService.getAll());
	}

	@GetMapping("/get-one")
	public Receptionist getById(Principal principal) {
		String username = principal.getName();
		logger.info("Receptionist is requesting theiw own profile");
		return receptionistService.getReceptionistByUsername(username);
	}

	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody Receptionist updated) {
		String username=principal.getName();
		logger.info("Receptionist is updating their own profile");
		return ResponseEntity.status(HttpStatus.OK).body(receptionistService.update(username, updated));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(Principal principal) {
		String username=principal.getName();
		receptionistService.delete(username);
		return ResponseEntity.ok("Receptionist deleted");
	}
	
	@PostMapping("/upload/profile-pic")
	public Receptionist uploadProfilePic(Principal principal, @RequestParam("file") MultipartFile file) throws IOException {
		return receptionistService.uploadProfilePic(file, principal.getName());
	}

	@PostMapping("/upload/profile-pic/{id}")
	public Receptionist uploadProfilePic(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
		return receptionistService.uploadProfilePicById(file, id);
	}
}
