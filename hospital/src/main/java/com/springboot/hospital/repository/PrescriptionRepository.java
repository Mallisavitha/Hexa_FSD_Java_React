package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {

	@Query("select p from Prescription p where p.consultation.consultationId=?1")
	List<Prescription> getByConsultationId(int consultationId);

}
