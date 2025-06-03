package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.ReceptionistRepository;


@Service
public class DoctorService {

	private DoctorRepository doctorRepository;
	private DepartmentRepository deptRepository;
	private ReceptionistRepository receptionistRepository;
	private UserService userService;

	public DoctorService(DoctorRepository doctorRepository, DepartmentRepository deptRepository,
			ReceptionistRepository receptionistRepository, UserService userService) {
		this.doctorRepository = doctorRepository;
		this.deptRepository = deptRepository;
		this.receptionistRepository = receptionistRepository;
		this.userService = userService;
	}

	public Doctor insertDoctor(int deptId, Doctor doctor, String receptionistUsername) {
		User user = doctor.getUser();
		user.setRole("DOCTOR");
		user = userService.signUp(user);
		doctor.setUser(user);
		Department dept = deptRepository.findById(deptId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid department ID"));

		Receptionist receptionist = receptionistRepository.getReceptionistByUsername(receptionistUsername);

		doctor.setDepartment(dept);
		doctor.setReceptionist(receptionist);

		return doctorRepository.save(doctor);
	}

	public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
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
		return doctorRepository.save(dbDoctor);
	}

	public void deleteDoctor(String username) {
		Doctor doctor=getDoctorByUsername(username);
		doctorRepository.delete(doctor);
	}

	public List<Doctor> getBySpecialization(String specialization) {
		return doctorRepository.getBySpecialization(specialization);
	}

	public Doctor getDoctorByUsername(String username) {
		return doctorRepository.getDoctorByUsername(username);
	}

	public List<Doctor> searchByName(String name) {
		// TODO Auto-generated method stub
		return doctorRepository.searchByName(name);
	}

}
