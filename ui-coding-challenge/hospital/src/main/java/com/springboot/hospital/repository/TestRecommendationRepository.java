package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.TestRecommendation;

public interface TestRecommendationRepository extends JpaRepository<TestRecommendation, Integer> {

	@Query("select t from TestRecommendation t where t.consultation.consultationId=?1")
	List<TestRecommendation> findByConsultationId(int consultationId);
}
