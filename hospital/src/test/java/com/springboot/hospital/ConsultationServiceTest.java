package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import com.springboot.hospital.dto.ConsultationDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.*;
import com.springboot.hospital.repository.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ConsultationServiceTest {

    @InjectMocks
    private com.springboot.hospital.service.ConsultationService consultationService;

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
    public void setUp() {
        doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setFullName("Dr. Smith");

        patient = new Patient();
        patient.setPatientId(10);
        patient.setFullName("Jane");

        appointment = new Appointment();
        appointment.setAppointmentId(100);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        consultation = new Consultation();
        consultation.setConsultationId(1);
        consultation.setAppointment(appointment);
        consultation.setSymptoms("Cough");
        consultation.setExamination("Normal");
        consultation.setTreatmentPlan("Rest");

        System.out.println("Test data initialized.");
    }

    @Test
    public void testAddConsultation_Success() {
        when(appointmentRepository.findById(100)).thenReturn(Optional.of(appointment));
        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.empty());
        when(consultationRepository.save(consultation)).thenReturn(consultation);

        Consultation result = consultationService.addConsultation(100, consultation);

        assertNotNull(result);
        assertEquals("Cough", result.getSymptoms());
    }

    @Test
    public void testAddConsultation_AlreadyExists() {
        when(appointmentRepository.findById(100)).thenReturn(Optional.of(appointment));
        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.of(consultation));

        assertThrows(RuntimeException.class, () -> {
            consultationService.addConsultation(100, consultation);
        });
    }

    @Test
    public void testUpdateConsultation_Success() {
        Consultation updated = new Consultation();
        updated.setSymptoms("Fever");
        updated.setExamination("Severe");
        updated.setTreatmentPlan("Antibiotics");

        when(consultationRepository.findById(100)).thenReturn(Optional.of(consultation));
        when(consultationRepository.save(consultation)).thenReturn(consultation);

        Consultation result = consultationService.updateConsultation(100, updated);

        assertEquals("Fever", result.getSymptoms());
        assertEquals("Severe", result.getExamination());
    }

    @Test
    public void testUpdateConsultation_NotFound() {
        when(consultationRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.updateConsultation(999, new Consultation());
        });

        assertEquals("Consultation not found", ex.getMessage());
    }

    @Test
    public void testGetForDoctorByAppointmentId_Valid() {
        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.of(consultation));
        when(doctorRepository.getDoctorByUsername("drsmith")).thenReturn(doctor);
        when(consultationDto.convertConsultationIntoDto(List.of(consultation))).thenReturn(List.of(new ConsultationDto()));

        ConsultationDto result = consultationService.getForDoctorByAppointmentId(100, "drsmith");
        assertNotNull(result);
    }

    @Test
    public void testGetForDoctorByAppointmentId_NotAuthorized() {
        Doctor otherDoctor = new Doctor();
        otherDoctor.setDoctorId(2);

        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.of(consultation));
        when(doctorRepository.getDoctorByUsername("wrongdoc")).thenReturn(otherDoctor);

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.getForDoctorByAppointmentId(100, "wrongdoc");
        });
    }

    @Test
    public void testGetForPatientByAppointmentId_Valid() {
        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.of(consultation));
        when(patientRepository.getPatientByUsername("jane")).thenReturn(patient);
        when(consultationDto.convertConsultationIntoDto(List.of(consultation))).thenReturn(List.of(new ConsultationDto()));

        ConsultationDto result = consultationService.getForPatientByAppointmentId(100, "jane");
        assertNotNull(result);
    }

    @Test
    public void testGetForPatientByAppointmentId_NotAuthorized() {
        Patient anotherPatient = new Patient();
        anotherPatient.setPatientId(20);

        when(consultationRepository.findByAppointmentId(100)).thenReturn(Optional.of(consultation));
        when(patientRepository.getPatientByUsername("otherpatient")).thenReturn(anotherPatient);

        assertThrows(ResourceNotFoundException.class, () -> {
            consultationService.getForPatientByAppointmentId(100, "otherpatient");
        });
    }

    @AfterEach
    public void tearDown() {
        consultation = null;
        appointment = null;
        doctor = null;
        patient = null;
        System.out.println("Test data cleared.");
    }
}
