package com.springboot.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Patient;


public interface PatientRepository extends JpaRepository<Patient, Integer>{

	@Query("select l from Patient l where l.user.username=?1")
	Patient getPatientByUsername(String username);

}
