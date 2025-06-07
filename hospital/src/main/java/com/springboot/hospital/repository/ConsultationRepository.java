package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Consultation;

public interface ConsultationRepository extends JpaRepository<Consultation, Integer>{


	@Query("SELECT c FROM Consultation c WHERE c.appointment.appointmentId = ?1")
	Optional<Consultation> findByAppointmentId(int appointmentId);

}
