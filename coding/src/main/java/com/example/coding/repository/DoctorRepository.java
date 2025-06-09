package com.example.coding.repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.coding.model.Doctor;


public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
	@Query("select l from Doctor l where l.user.username=?1")
	Doctor getDoctorByUsername(String username);

}
