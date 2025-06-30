package com.springboot.hospital.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	public List<TestRecommendation> getbyConsultation(int consultationId) {
		return testRepository.findByConsultationId(consultationId);
	}

	public TestRecommendation updateTest(int testId, TestRecommendation updated, String username) {
		TestRecommendation test = testRepository.findById(testId)
				.orElseThrow(() -> new ResourceNotFoundException("Test not found"));

		LabStaff labStaff = labStaffRepository.findByUserUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("LabStaff not found for this user"));

		if (updated.getStatus() != null)
			test.setStatus(updated.getStatus());

		if (updated.getReportDownload() != null)
			test.setReportDownload(updated.getReportDownload());

		test.setLabStaff(labStaff);

		return testRepository.save(test);
	}

	public List<TestRecommendation> getAllTests() {
		// TODO Auto-generated method stub
		return testRepository.findAll();
	}

	public TestRecommendation uploadReport(int testId, MultipartFile file) throws IOException {
		TestRecommendation test = testRepository.findById(testId)
				.orElseThrow(() -> new ResourceNotFoundException("Test not found"));

		String originalFileName = file.getOriginalFilename();
		String extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();

		if (!extension.equals("pdf")) {
			throw new RuntimeException("Only PDF files are allowed");
		}

		long kbs = file.getSize() / 1024;
		if (kbs > 5000) {
			throw new RuntimeException("PDF too large. Max allowed size is 5MB");
		}

		String uploadFolder = "D:\\JavaFSD\\nodejs\\react-hospital-ui\\public\\reports";
		Files.createDirectories(Path.of(uploadFolder));

		Path path = Paths.get(uploadFolder, originalFileName);
		Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

		test.setReportDownload(originalFileName);
		test.setStatus(TestRecommendation.TestStatus.COMPLETED);
		return testRepository.save(test);
	}

	public void deleteReport(int testId) {
		TestRecommendation test = testRepository.findById(testId)
				.orElseThrow(() -> new ResourceNotFoundException("Test not found"));

		String fileName = test.getReportDownload();
		if (fileName != null) {
			Path path = Paths.get("D:\\JavaFSD\\nodejs\\react-hospital-ui\\public\\reports", fileName);
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				throw new RuntimeException("Failed to delete the file from disk", e);
			}
		}

		test.setReportDownload(null);
		test.setStatus(TestRecommendation.TestStatus.PENDING);
		testRepository.save(test);
	}

}
