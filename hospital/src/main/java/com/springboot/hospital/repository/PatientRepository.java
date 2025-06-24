package com.springboot.hospital.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.hospital.model.Patient;


public interface PatientRepository extends JpaRepository<Patient, Integer>{

	@Query("select l from Patient l where l.user.username=?1")
	Patient getPatientByUsername(String username);

	@Query("select a.patient from Appointment a where a.doctor.specialization=?1")
	List<Patient> getPatientsBySpecialization(String specialization);

	@Query("select a.patient from Appointment a where a.scheduledDate=?1")
	List<Patient> getPatientByAppointmentDate(LocalDate parseDate);

	@Query("SELECT p FROM Patient p WHERE LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Patient> getPatientByName(@Param("name") String name);

	

}
