package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.ReceptionistRepository;

@Service
public class LabStaffService {

	private LabStaffRepository labRepository;
	private DepartmentRepository departmentRepository;
	private ReceptionistRepository receptionistRepository;
	private UserService userService;

	public LabStaffService(LabStaffRepository labRepository, DepartmentRepository departmentRepository,
			ReceptionistRepository receptionistRepository, UserService userService) {
		this.labRepository = labRepository;
		this.departmentRepository = departmentRepository;
		this.receptionistRepository = receptionistRepository;
		this.userService = userService;
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

	public List<LabStaff> getAll() {
		return labRepository.findAll();
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

	        return labRepository.save(labStaff);
	    }

}
