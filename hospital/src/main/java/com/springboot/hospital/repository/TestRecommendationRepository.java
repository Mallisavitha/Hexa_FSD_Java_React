package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.TestRecommendation;

public interface TestRecommendationRepository extends JpaRepository<TestRecommendation, Integer> {

	@Query("SELECT t FROM TestRecommendation t WHERE t.consultation.consultationId = ?1")
    List<TestRecommendation> getByConsultation(int consultationId);
	
	@Query("SELECT t FROM TestRecommendation t WHERE t.labStaff.labStaffId = ?1")
    List<TestRecommendation> getByLabStaff(int labStaffId);
}
