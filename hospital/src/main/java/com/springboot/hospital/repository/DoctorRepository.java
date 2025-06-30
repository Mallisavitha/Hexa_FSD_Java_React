package com.springboot.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.hospital.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{

	@Query("SELECT d FROM Doctor d WHERE d.specialization = ?1")
    List<Doctor> getBySpecialization(String specialization);

	@Query("select l from Doctor l where l.user.username=?1")
	Doctor getDoctorByUsername(String username);

	Optional<Doctor> findByUserUsername(String username);

	@Query("SELECT d FROM Doctor d WHERE LOWER(d.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Doctor> getDoctorByName(@Param("name") String name);

	@Query("SELECT DISTINCT d.specialization FROM Doctor d")
	List<String> findAllDistinctSpecializations();

	

}
