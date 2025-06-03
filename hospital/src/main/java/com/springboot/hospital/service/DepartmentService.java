package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.repository.DepartmentRepository;

@Service
public class DepartmentService {
	
	private DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public Department addDepartment(Department department) {
		return departmentRepository.save(department);
	}

	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}

	public Department getById(int id) {
		return departmentRepository.findById(id).orElseThrow(() ->  new ResourceNotFoundException("Department not found"));
	}

	public Department getByName(String name) {
		return departmentRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Department not found with name: "+name));
	}

	public Department updateDepartment(int id, Department updated) {
		Department dbDept = getById(id);
        if (updated.getName() != null) {
            dbDept.setName(updated.getName());
        }
        return departmentRepository.save(dbDept);
	}

	public void deleteDepartment(int id) {
		getById(id);
		departmentRepository.deleteById(id);
	}

}
