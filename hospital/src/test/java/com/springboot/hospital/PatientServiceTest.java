package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.PatientDto;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.service.PatientService;
import com.springboot.hospital.service.UserService;

@SpringBootTest
class PatientServiceTest {

	@InjectMocks
	private PatientService patientService;
	@Mock
	private PatientRepository patientRepository;
	@Mock
	private UserService userService;

	private Patient patient;
	private User user;

	@BeforeEach
	public void init() {
		user = new User();
		user.setUsername("john123");
		user.setPassword("secret");

		patient = new Patient();
		patient.setPatientId(1);
		patient.setFullName("John Doe");
		patient.setDob(LocalDate.of(2000, 1, 1));
		patient.setGender("Male");
		patient.setContactNumber("9876543210");
		patient.setUser(user);

		System.out.println("Patient object created: " + patient);
	}

	@Test
	public void insertPatientTest() {
		when(userService.signUp(user)).thenReturn(user);
		when(patientRepository.save(patient)).thenReturn(patient);

		Patient saved = patientService.insertPatient(patient);
		assertNotNull(saved);
		verify(userService).signUp(user);
		verify(patientRepository).save(patient);
	}

	@Test
	public void getAllPatientTest() {
		List<Patient> list = List.of(patient);

		when(patientRepository.findAll()).thenReturn(list);

		List<PatientDto> result = patientService.getAllPatient();

		assertEquals(1, result.size());
		assertEquals(patient.getFullName(), result.get(0).getFullName());

		verify(patientRepository).findAll();
	}

	@Test
	public void getPatientByIdTest() {
		when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

		Patient found = patientService.getPatientById(1);
		assertEquals("John Doe", found.getFullName());
	}

	@Test
	public void updatePatientTest() {
		Patient updated = new Patient();
		updated.setFullName("Jane");
		updated.setContactNumber("9638527485");

		when(patientRepository.getPatientByUsername("john123")).thenReturn(patient);
		when(patientRepository.save(patient)).thenReturn(patient);

		Patient result = patientService.updatePatient("john123", updated);

		assertEquals("Jane", result.getFullName());
		assertEquals("9638527485", result.getContactNumber());
	}

	@AfterEach
	public void afterTest() {
		patient = null;
		user = null;
		System.out.println("Objects released.");
	}

}
