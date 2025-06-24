package com.example.coding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.coding.model.MedicalHistory;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Integer>{


//	@Query("select m from MedicalHistory m where m.patient.patientId=?1")
//	List<MedicalHistory> findByPatientId(int patientId);

}
