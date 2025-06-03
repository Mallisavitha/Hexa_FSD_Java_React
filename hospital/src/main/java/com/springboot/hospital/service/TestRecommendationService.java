package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.TestRecommendation;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.TestRecommendationRepository;

@Service
public class TestRecommendationService {
	
	private TestRecommendationRepository testRepository;
	private ConsultationRepository consultationRepository;
	private LabStaffRepository labStaffRepository;

	public TestRecommendationService(TestRecommendationRepository testRepository,
			ConsultationRepository consultationRepository, LabStaffRepository labStaffRepository) {
		this.testRepository = testRepository;
		this.consultationRepository = consultationRepository;
		this.labStaffRepository = labStaffRepository;
	}

	public TestRecommendation addTest(int consultationId, TestRecommendation test) {
		Consultation consultation=consultationRepository.findById(consultationId).orElseThrow(() -> new ResourceNotFoundException("Invalid consultation ID"));
		test.setConsultation(consultation);
		test.setStatus(TestRecommendation.TestStatus.PENDING);
		
		return testRepository.save(test);
	}

	public TestRecommendation getById(int id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));
    }
	
	public TestRecommendation assignLabStaff(int testId, int labStaffId) {
		TestRecommendation test = getById(testId);
        LabStaff labStaff = labStaffRepository.findById(labStaffId).orElseThrow(() -> new ResourceNotFoundException("Invalid lab staff ID"));

        test.setLabStaff(labStaff);
        test.setStatus(TestRecommendation.TestStatus.IN_PROGRESS);
        return testRepository.save(test);
	}

	public TestRecommendation markCompleted(int testId, String reportDownload) {
		TestRecommendation test = getById(testId);
        test.setStatus(TestRecommendation.TestStatus.COMPLETED);
        test.setReportDownload(reportDownload);
        return testRepository.save(test);
	}

	public List<TestRecommendation> getAll() {
		return testRepository.findAll();
	}

	public List<TestRecommendation> getByConsultation(int consultationId) {
		return testRepository.getByConsultation(consultationId);
	}

	public List<TestRecommendation> getByLabStaff(int labStaffId) {
		return testRepository.getByLabStaff(labStaffId);
	}

	public void delete(int id) {
		getById(id);
		testRepository.deleteById(id);
	}
	

}
