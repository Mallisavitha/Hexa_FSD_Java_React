package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.TestRecommendationDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.TestRecommendation;
import com.springboot.hospital.model.TestRecommendation.TestStatus;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.TestRecommendationRepository;
import com.springboot.hospital.service.TestRecommendationService;

@SpringBootTest
public class TestRecommendationServiceTest {

	@InjectMocks
	private TestRecommendationService testRecommendationService;

	@Mock
	private TestRecommendationRepository testRepository;

	@Mock
	private ConsultationRepository consultationRepository;

	@Mock
	private LabStaffRepository labStaffRepository;

	@Mock
	private TestRecommendationDto testDto;

	private TestRecommendation testRecommendation;
	private Consultation consultation;
	private LabStaff labStaff;

	@BeforeEach
	public void init() {
		consultation = new Consultation();
		consultation.setConsultationId(1);
		consultation.setSymptoms("Headache");

		labStaff = new LabStaff();
		labStaff.setLabStaffId(10);

		testRecommendation = new TestRecommendation();
		testRecommendation.setTestId(100);
		testRecommendation.setTestName("Blood Test");
		testRecommendation.setStatus(TestStatus.PENDING);
		testRecommendation.setConsultation(consultation);
	}

	@Test
	public void recommendTestSuccessTest() {
		when(consultationRepository.findById(1)).thenReturn(Optional.of(consultation));
		when(testRepository.save(any(TestRecommendation.class))).thenReturn(testRecommendation);

		TestRecommendation result = testRecommendationService.recommendTest(1, testRecommendation);

		assertNotNull(result);
		assertEquals("Blood Test", result.getTestName());
		assertEquals(TestStatus.PENDING, result.getStatus());
		assertEquals(consultation, result.getConsultation());
	}

	@Test
	public void recommendTestInvalidConsultationIdTest() {
		when(consultationRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			testRecommendationService.recommendTest(99, testRecommendation);
		});
	}

	@Test
	public void getByConsultationSuccessTest() {
		when(testRepository.findByConsultationId(1)).thenReturn(List.of(testRecommendation));
		when(testDto.convertTestIntoDto(anyList())).thenReturn(List.of(new TestRecommendationDto()));

		List<TestRecommendation> result = testRecommendationService.getbyConsultation(1);

		assertEquals(1, result.size());
	}

	@Test
	public void updateTestSuccessTest() {
		TestRecommendation updated = new TestRecommendation();
		updated.setStatus(TestStatus.COMPLETED);
		updated.setReportDownload("report123.pdf");

		when(testRepository.findById(100)).thenReturn(Optional.of(testRecommendation));
		when(labStaffRepository.findByUserUsername("labuser")).thenReturn(Optional.of(labStaff));
		when(testRepository.save(any(TestRecommendation.class))).thenReturn(testRecommendation);

		TestRecommendation result = testRecommendationService.updateTest(100, updated, "labuser");

		assertEquals(TestStatus.COMPLETED, result.getStatus());
		assertEquals("report123.pdf", result.getReportDownload());
		assertEquals(labStaff, result.getLabStaff());
	}

	@Test
	public void updateTestNotFoundTest() {
		when(testRepository.findById(404)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			testRecommendationService.updateTest(404, new TestRecommendation(), "labuser");
		});
	}

	@Test
	public void updateTestLabStaffNotFoundTest() {
		when(testRepository.findById(100)).thenReturn(Optional.of(testRecommendation));
		when(labStaffRepository.findByUserUsername("wronguser")).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> {
			testRecommendationService.updateTest(100, new TestRecommendation(), "wronguser");
		});
	}

	@AfterEach
	public void afterTest() {
		consultation = null;
		testRecommendation = null;
		labStaff = null;
	}
}
