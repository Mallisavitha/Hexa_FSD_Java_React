package com.springboot.hospital.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


@Service
public class DoctorService {

	private DoctorRepository doctorRepository;
	private DepartmentRepository deptRepository;
	private ReceptionistRepository receptionistRepository;
	private UserService userService;
	private DoctorDto doctorDto;

	public DoctorService(DoctorRepository doctorRepository, DepartmentRepository deptRepository,
			ReceptionistRepository receptionistRepository, UserService userService, DoctorDto doctorDto) {
		super();
		this.doctorRepository = doctorRepository;
		this.deptRepository = deptRepository;
		this.receptionistRepository = receptionistRepository;
		this.userService = userService;
		this.doctorDto = doctorDto;
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

	public List<?> getAllDoctors(int page, int size) {
		//Activate Pageable interface
		Pageable pageable=PageRequest.of(page, size);
		List<Doctor> list=doctorRepository.findAll(pageable).getContent();
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
		return doctorRepository.save(dbDoctor);
	}

	public void deleteDoctor(String username) {
		Doctor doctor=getDoctorByUsername(username);
		doctorRepository.delete(doctor);
	}

	public List<DoctorDto> getBySpecialization(String specialization) {
		List<Doctor> list=doctorRepository.getBySpecialization(specialization);
		return doctorDto.convertDoctorIntoDto(list);
	}

	public Doctor getDoctorByUsername(String username) {
		return doctorRepository.getDoctorByUsername(username);
	}

	public List<DoctorDto> searchByName(String name) {
		List<Doctor> list=doctorRepository.searchByName(name);
		return doctorDto.convertDoctorIntoDto(list);
	}

}
