package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{

	Optional<Department> findByName(String name);

}
