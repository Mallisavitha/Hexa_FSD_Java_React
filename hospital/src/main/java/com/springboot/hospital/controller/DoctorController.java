package com.springboot.hospital.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

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

import com.springboot.hospital.dto.DoctorDto;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.service.DoctorService;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	Logger logger = LoggerFactory.getLogger("DoctorController");

	@PostMapping("/add/{deptId}")
	public ResponseEntity<?> insertDoctor(@PathVariable int deptId, Principal principal, @RequestBody Doctor doctor) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(doctorService.insertDoctor(deptId, doctor, principal.getName()));
	}

	@GetMapping("/get-all")
	public List<?> getAllDoctors(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "1000000") Integer size) {
		if (page == 0 && size == 1000000)
			logger.info("No Pagination call for all courses");
		return doctorService.getAllDoctors(page, size);
	}

	@GetMapping("/get-one")
	public Doctor getDoctorById(Principal principal) {
		String username = principal.getName();
		logger.info("Doctor is requesting their own profile");
		return doctorService.getDoctorByUsername(username);
	}

	@PutMapping("/update")
	public ResponseEntity<?> update(Principal principal, @RequestBody Doctor updatedDoctor) {
		String username = principal.getName();
		logger.info("Doctor is updating their profile");
		return ResponseEntity.ok(doctorService.updateDoctor(username, updatedDoctor));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateDoctorById(@PathVariable int id, @RequestBody Doctor updatedDoctor) {
		logger.info("Admin/Receptionist updating doctor with ID: " + id);
		return ResponseEntity.ok(doctorService.updateDoctorById(id, updatedDoctor));
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(Principal principal) {
		String username = principal.getName();
		doctorService.deleteDoctor(username);
		return ResponseEntity.ok("Doctor deleted");
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable int id) {
		doctorService.deleteDoctorById(id);
		return ResponseEntity.ok("Doctor deleted");
	}

	@GetMapping("/specialization/{specialization}")
	public ResponseEntity<?> getBySpecialization(@PathVariable String specialization) {
		return ResponseEntity.ok(doctorService.getBySpecialization(specialization));
	}

	@GetMapping("/specialization")
	public ResponseEntity<?> getAllSpecializations() {
	    return ResponseEntity.ok(doctorService.getAllSpecializations());
	}


	@PostMapping("/upload/profile-pic")
	public Doctor uploadProfilePic(Principal principal, @RequestParam("file") MultipartFile file) throws IOException {
		return doctorService.uploadProfilePic(file, principal.getName());
	}

	@PostMapping("/upload/profile-pic/{id}")
	public Doctor uploadProfilePic(@PathVariable int id, @RequestParam("file") MultipartFile file) throws IOException {
		return doctorService.uploadProfilePicById(file, id);
	}
	
	@GetMapping("/search/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name){
		return ResponseEntity.status(HttpStatus.OK).body(doctorService.getDoctorByName(name));
	}
	

}
