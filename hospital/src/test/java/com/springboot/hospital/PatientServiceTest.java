package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.springboot.hospital.dto.PatientDto;
import com.springboot.hospital.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.repository.UserRepository;
import com.springboot.hospital.service.PatientService;
import com.springboot.hospital.service.UserService;

@SpringBootTest
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private PatientDto patientDto;

    private Patient patient;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("john123");
        user.setPassword("password");
        user.setRole("PATIENT");

        patient = new Patient();
        patient.setPatientId(1);
        patient.setFullName("John Doe");
        patient.setDob(LocalDate.of(1990, 5, 20));
        patient.setGender("Male");
        patient.setContactNumber("9876543210");
        patient.setUser(user);
    }

    @Test
    public void testInsertPatient_Success() {
        when(userService.signUp(user)).thenReturn(user);
        when(patientRepository.save(patient)).thenReturn(patient);

        Patient result = patientService.insertPatient(patient);
        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
        assertEquals("PATIENT", result.getUser().getRole());
    }

    @Test
    public void testGetPatientById_Success() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        Patient result = patientService.getPatientById(1);
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    public void testGetPatientById_NotFound() {
        when(patientRepository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.getPatientById(2);
        });

        assertEquals("Patient ID Not Found 2", exception.getMessage());
    }

    @Test
    public void testUpdatePatient_Success() {
        when(patientRepository.getPatientByUsername("john123")).thenReturn(patient);
        Patient update = new Patient();
        update.setFullName("Updated Name");
        update.setDob(LocalDate.of(1991, 1, 1));
        update.setGender("Male");
        update.setContactNumber("9999999999");

        when(patientRepository.save(patient)).thenReturn(patient);

        Patient updated = patientService.updatePatient("john123", update);
        assertEquals("Updated Name", updated.getFullName());
        assertEquals("9999999999", updated.getContactNumber());
    }

    @Test
    public void testDeletePatient_Success() {
        when(patientRepository.getPatientByUsername("john123")).thenReturn(patient);
        // No exception means success
        patientService.deletePatient("john123");
    }

    @AfterEach
    public void teardown() {
        patient = null;
        user = null;
    }
}
