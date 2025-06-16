package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.springboot.hospital.dto.ConsultationDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Consultation;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.ConsultationRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.service.ConsultationService;

@SpringBootTest
public class ConsultationServiceTest {

	@InjectMocks
	private ConsultationService consultationService;
	@Mock
	private ConsultationRepository consultationRepository;
	@Mock
	private AppointmentRepository appointmentRepository;
	@Mock
	private ConsultationDto consultationDto;
	@Mock
	private PatientRepository patientRepository;
	@Mock
	private DoctorRepository doctorRepository;

	private Appointment appointment;
	private Consultation consultation;
	private Doctor doctor;
	private Patient patient;

	@BeforeEach
	public void init() {
		doctor = new Doctor();
		doctor.setDoctorId(1);
		doctor.setFullName("Smith");

		patient = new Patient();
		patient.setPatientId(2);
		patient.setFullName("John");

		appointment = new Appointment();
		appointment.setAppointmentId(10);
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);

		consultation = new Consultation();
		consultation.setConsultationId(20);
		consultation.setSymptoms("Fever");
		consultation.setExamination("Normal");
		consultation.setTreatmentPlan("Paracetamol");
		consultation.setAppointment(appointment);

		System.out.println("Test data initialized.");
	}

	@Test
	public void addConsultationSuccessTest() {
		when(appointmentRepository.findById(10)).thenReturn(Optional.of(appointment));
		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.empty());
		when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

		Consultation result = consultationService.addConsultation(10, new Consultation());

		assertNotNull(result);
		verify(consultationRepository, times(1)).save(any(Consultation.class));
	}

	@Test
	public void addConsultationExistsTest() {
		when(appointmentRepository.findById(10)).thenReturn(Optional.of(appointment));
		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.of(consultation));

		RuntimeException ex = assertThrows(RuntimeException.class, () -> {
			consultationService.addConsultation(10, new Consultation());
		});
		assertEquals("Consultation already exists for this appointment", ex.getMessage());
	}

	@Test
	public void getAllConsultationTest() {
		List<Consultation> list = List.of(consultation);
		List<ConsultationDto> dtoList = List.of(new ConsultationDto());

		when(consultationRepository.findAll()).thenReturn(list);
		when(consultationDto.convertConsultationIntoDto(list)).thenReturn(dtoList);

		List<ConsultationDto> result = consultationService.getAll();

		assertEquals(1, result.size());
	}

	@Test
	public void getForDoctorByAppointmentIdSuccessTest() {
		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.of(consultation));
		when(doctorRepository.getDoctorByUsername("drsmith")).thenReturn(doctor);
		when(consultationDto.convertConsultationIntoDto(List.of(consultation)))
				.thenReturn(List.of(new ConsultationDto()));

		ConsultationDto result = consultationService.getForDoctorByAppointmentId(10, "drsmith");

		assertNotNull(result);
	}

	@Test
	public void getForDoctorByAppointmentIdUnathorizesTest() {
		Doctor anotherDoctor = new Doctor();
		anotherDoctor.setDoctorId(99);

		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.of(consultation));
		when(doctorRepository.getDoctorByUsername("otherdoc")).thenReturn(anotherDoctor);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			consultationService.getForDoctorByAppointmentId(10, "otherdoc");
		});

		assertEquals("Doctor not authorized to view this consultation", ex.getMessage());
	}

	@Test
	public void updateConsultationSuccessTest() {
		Consultation updated = new Consultation();
		updated.setSymptoms("Cold");
		updated.setExamination("Mild");
		updated.setTreatmentPlan("Rest");

		when(consultationRepository.findById(10)).thenReturn(Optional.of(consultation));
		when(consultationRepository.save(any(Consultation.class))).thenReturn(consultation);

		Consultation result = consultationService.updateConsultation(10, updated);

		assertEquals("Cold", result.getSymptoms());
		assertEquals("Mild", result.getExamination());
		assertEquals("Rest", result.getTreatmentPlan());
	}

	@Test
	public void updateConsultationNotFoundTest() {
		when(consultationRepository.findById(99)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			consultationService.updateConsultation(99, new Consultation());
		});
		assertEquals("Consultation not found", ex.getMessage());

	}

	@Test
	public void getByAppointmentIdSuccessTest() {
		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.of(consultation));
		when(patientRepository.getPatientByUsername("johndoe")).thenReturn(patient);
		when(consultationDto.convertConsultationIntoDto(List.of(consultation)))
				.thenReturn(List.of(new ConsultationDto()));

		ConsultationDto result = consultationService.getForPatientByAppointmentId(10, "johndoe");

		assertNotNull(result);
	}

	@Test
	public void getByAppointmentIdUnauthorizedTest() {
		Patient anotherPatient = new Patient();
		anotherPatient.setPatientId(999);

		when(consultationRepository.findByAppointmentId(10)).thenReturn(Optional.of(consultation));
		when(patientRepository.getPatientByUsername("another")).thenReturn(anotherPatient);

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			consultationService.getForPatientByAppointmentId(10, "another");
		});

		assertEquals("Patient not authorized to view this consultation", ex.getMessage());
	}
	
	@AfterEach
	public void afterTest() {
		consultation=null;
		appointment=null;
		doctor=null;
		patient=null;
		System.out.println("Test data cleared.");
	}

}
