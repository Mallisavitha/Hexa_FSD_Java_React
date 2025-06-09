package com.example.coding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coding.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer>{

	@Query("select l from Patient l where l.user.username=?1")
	Patient getPatientByUsername(String username);

	@Query("SELECT a.patient FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") int doctorId);
}

