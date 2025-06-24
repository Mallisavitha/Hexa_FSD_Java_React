package com.example.coding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coding.model.Doctor.Speciality;
import com.example.coding.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer>{

	@Query("select l from Patient l where l.user.username=?1")
	Patient getPatientByUsername(String username);

	@Query("SELECT a.patient FROM Appointment a WHERE a.doctor.id = ?1")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") int doctorId);

	@Query("select a.patient from Appointment a where a.doctor.speciality=?1")
	List<Patient> getPatientByDoctorSpeciality(Speciality speciality);
}

