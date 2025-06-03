package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer>{

	@Query("select c from Consultation c where c.appointment.doctor.doctorId=?1")
	List<Consultation> getByPatientId(int patientId);

	@Query("select c from Consultation c where c.appointment.doctor.doctorId=?1")
	List<Consultation> getByDoctorId(int doctorId);

}
