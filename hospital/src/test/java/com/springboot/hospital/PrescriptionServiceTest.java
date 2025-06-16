package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.PrescriptionDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.Prescription;
import com.springboot.hospital.model.Prescription.MealTime;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.PrescriptionRepository;
import com.springboot.hospital.service.PrescriptionService;

@SpringBootTest
public class PrescriptionServiceTest {

	@InjectMocks
	private PrescriptionService prescriptionService;
	@Mock
	private PrescriptionRepository prescriptionRepository;
	@Mock
	private ConsultationRepository consultationRepository;
	@Mock
	private PrescriptionDto prescriptionDto;

	private Prescription prescription;
	private Consultation consultation;

	@BeforeEach
	void setUp() {
		consultation = new Consultation();
		consultation.setConsultationId(1);
		consultation.setSymptoms("Fever");

		prescription = new Prescription();
		prescription.setPrescriptionId(101);
		prescription.setMedicineName("Paracetamol");
		prescription.setDosageTiming("Morning");
		prescription.setDuration("5 days");
		prescription.setMealTime(MealTime.AF);
		prescription.setConsultation(consultation);
	}

	@Test
	void addPrescriptionSuccessTest() {
		when(consultationRepository.findById(1)).thenReturn(Optional.of(consultation));
		when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

		Prescription saved = prescriptionService.addPrescription(1, prescription);

		assertNotNull(saved);
		assertEquals("Paracetamol", saved.getMedicineName());
		verify(prescriptionRepository, times(1)).save(prescription);
	}

	@Test
	void addPrescriptionInvalidConsultationIdTest() {
		when(consultationRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> prescriptionService.addPrescription(99, prescription));
	}

	@Test
	void getByConsultationSuccessTest() {
		when(prescriptionRepository.getByConsultationId(1)).thenReturn(List.of(prescription));
		when(prescriptionDto.convertPrescriptionIntoDto(anyList())).thenReturn(List.of(new PrescriptionDto()));

		List<PrescriptionDto> result = prescriptionService.getByConsultation(1);
		assertEquals(1, result.size());
	}

	@Test
	void testUpdatePrescriptionSuccess() {
		when(prescriptionRepository.findById(101)).thenReturn(Optional.of(prescription));
		when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescription);

		Prescription updated = new Prescription();
		updated.setMedicineName("Ibuprofen");
		updated.setDosageTiming("Night");
		updated.setDuration("3 days");
		updated.setMealTime(MealTime.BF);

		Prescription result = prescriptionService.updatePrescription(101, updated);

		assertEquals("Ibuprofen", result.getMedicineName());
		assertEquals("Night", result.getDosageTiming());
		assertEquals("3 days", result.getDuration());
		assertEquals(MealTime.BF, result.getMealTime());

	}
	
	@AfterEach
	public void afterTest() {
		prescription=null;
		consultation=null;
	}

}
