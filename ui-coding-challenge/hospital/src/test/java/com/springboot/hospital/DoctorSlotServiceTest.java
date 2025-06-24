package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.DoctorSlotDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.DoctorSlotRepository;
import com.springboot.hospital.service.DoctorSlotService;

@SpringBootTest
public class DoctorSlotServiceTest {

	@InjectMocks
	private DoctorSlotService doctorSlotService;
	@Mock
	private DoctorSlotRepository doctorSlotRepository;
	@Mock
	private DoctorRepository doctorRepository;
	@Mock
	private DoctorSlotDto doctorSlotDto;

	private Doctor doctor;
	private DoctorSlot doctorSlot;

	@BeforeEach
	public void init() {
		User user = new User();
		user.setUsername("docuser");

		doctor = new Doctor();
		doctor.setDoctorId(1);
		doctor.setFullName("Dr. Who");
		doctor.setUser(user);

		doctorSlot = new DoctorSlot();
		doctorSlot.setSlotId(101);
		doctorSlot.setDate(LocalDate.now());
		doctorSlot.setTime(LocalTime.of(10, 0));
		doctorSlot.setMaxAppointment(5);
		doctorSlot.setBookedCount(0);
		doctorSlot.setDoctor(doctor);
	}

	@Test
	public void addSlotSuccessTest() {
		when(doctorRepository.findByUserUsername("docuser")).thenReturn(Optional.of(doctor));
		when(doctorSlotRepository.save(doctorSlot)).thenReturn(doctorSlot);

		DoctorSlot saved = doctorSlotService.addSlot("docuser", doctorSlot);

		assertNotNull(saved);
		assertEquals(doctor, saved.getDoctor());
	}

	@Test
	public void addSlotDoctotNotFoundTest() {
		when(doctorRepository.findByUserUsername("invalid")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> doctorSlotService.addSlot("invalid", doctorSlot));

		assertEquals("Doctor Not Found".toLowerCase(), ex.getMessage().toLowerCase());
	}

	@Test
	public void getSlotsByDoctorUsernameSuccessTest() {
		when(doctorRepository.findByUserUsername("docuser")).thenReturn(Optional.of(doctor));
		when(doctorSlotRepository.findByDoctorId(1)).thenReturn(Arrays.asList(doctorSlot));

		List<DoctorSlot> slots = doctorSlotService.getSlotsByDoctorUsername("docuser");

		assertEquals(1, slots.size());
		assertEquals(doctorSlot, slots.get(0));
	}

	@Test
	public void getSlotByDoctorUsernameNotFoundTest() {
		when(doctorRepository.findByUserUsername("invalid")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> doctorSlotService.getSlotsByDoctorUsername("invalid"));

		assertTrue(ex.getMessage().toLowerCase().contains("doctor not found"));
	}

	@Test
	public void getAllSlotTest() {
		when(doctorSlotRepository.findAll()).thenReturn(Arrays.asList(doctorSlot));
		when(doctorSlotDto.convertSlotIntoDto(anyList())).thenReturn(List.of(new DoctorSlotDto()));

		List<DoctorSlotDto> dtos = doctorSlotService.getAllSlots();
		assertEquals(1, dtos.size());
	}

	@AfterEach
	public void afterTest() {
		doctor = null;
		doctorSlot = null;
	}

}
