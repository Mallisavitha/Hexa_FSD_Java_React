package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.TestRecommendationDto;
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
	private TestRecommendationDto testDto;

	public TestRecommendationService(TestRecommendationRepository testRepository,
			ConsultationRepository consultationRepository, LabStaffRepository labStaffRepository,
			TestRecommendationDto testDto) {
		super();
		this.testRepository = testRepository;
		this.consultationRepository = consultationRepository;
		this.labStaffRepository = labStaffRepository;
		this.testDto = testDto;
	}

	public TestRecommendation recommendTest(int consultationId, TestRecommendation test) {
		Consultation consultation = consultationRepository.findById(consultationId)
				.orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

		test.setConsultation(consultation);
		test.setStatus(TestRecommendation.TestStatus.PENDING);
		return testRepository.save(test);
	}

	public List<TestRecommendationDto> getbyConsultation(int consultationId) {
		List<TestRecommendation> list=testRepository.findByConsultationId(consultationId);
		return testDto.convertTestIntoDto(list);
	}

	public TestRecommendation updateTest(int testId, TestRecommendation updated, String username) {
		TestRecommendation test = testRepository.findById(testId).orElseThrow(() -> new ResourceNotFoundException("Test not found"));

		LabStaff labStaff = labStaffRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("LabStaff not found for this user"));

		if (updated.getStatus() != null)
			test.setStatus(updated.getStatus());

		if (updated.getReportDownload() != null)
			test.setReportDownload(updated.getReportDownload());

		test.setLabStaff(labStaff);

		return testRepository.save(test);
	}

}
