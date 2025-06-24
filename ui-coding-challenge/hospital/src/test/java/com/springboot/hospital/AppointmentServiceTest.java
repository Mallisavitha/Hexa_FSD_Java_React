package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.AppointmentDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.DoctorSlotRepository;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.service.AppointmentService;

@SpringBootTest
class AppointmentServiceTest {

	@InjectMocks
	private AppointmentService appointmentService;
	@Mock
	private AppointmentRepository appointmentRepository;
	@Mock
	private PatientRepository patientRepository;
	@Mock
	private AppointmentDto appointmentDto;
	@Mock
	private DoctorRepository doctorRepository;
	@Mock
	private DoctorSlotRepository doctorSlotRepository;

	private Appointment appointment;
	private DoctorSlot slot;
	private Doctor doctor;
	private Patient patient;

	@BeforeEach
	public void init() {
		doctor = new Doctor();
		doctor.setDoctorId(1);
		doctor.setFullName("John");

		patient = new Patient();
		patient.setPatientId(2);
		patient.setFullName("Jane Doe");
		patient.setUser(null);

		slot = new DoctorSlot();
		slot.setSlotId(3);
		slot.setDate(LocalDate.now());
		slot.setTime(LocalTime.of(10, 30));
		slot.setDoctor(doctor);
		slot.setMaxAppointment(2);
		slot.setBookedCount(1);

		appointment = new Appointment();
		appointment.setAppointmentId(1);
		appointment.setStatus(Appointment.Status.SCHEDULED);
		appointment.setScheduledDate(slot.getDate());
		appointment.setScheduledTime(slot.getTime());
		appointment.setDoctor(doctor);
		appointment.setDoctorSlot(slot);
		appointment.setPatient(patient);

		System.out.println("Test data initialized.");
	}

	@Test
	public void bookAppointment_Valid_Test() {
		when(patientRepository.getPatientByUsername("jane")).thenReturn(patient);
		when(doctorSlotRepository.findById(3)).thenReturn(Optional.of(slot));
		when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
		when(doctorSlotRepository.save(slot)).thenReturn(slot);

		Appointment result = appointmentService.bookAppointment("jane", 3, new Appointment());

		assertNotNull(result);
		assertEquals(Appointment.Status.SCHEDULED, result.getStatus());
		verify(doctorSlotRepository, times(1)).save(slot);
		verify(appointmentRepository, times(1)).save(any(Appointment.class));
	}

	@Test
	public void bookAppointment_SlotFull_Test() {
		slot.setBookedCount(2);
		when(doctorSlotRepository.findById(3)).thenReturn(Optional.of(slot));

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.bookAppointment("jane", 3, new Appointment());
		});

		assertEquals("Slot is fully booked.", ex.getMessage());
	}

	@Test
	public void bookAppointment_SlotNotFound_Test() {
		when(doctorSlotRepository.findById(99)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.bookAppointment("jane", 99, new Appointment());
		});

		assertEquals("Slot not found", ex.getMessage());
	}

	@Test
	public void getAppointmentsForPatient_Test() {
		List<Appointment> list = List.of(appointment);
		List<AppointmentDto> dtoList = List.of(new AppointmentDto());

		when(patientRepository.getPatientByUsername("jane")).thenReturn(patient);
		when(appointmentRepository.findByPatient(patient)).thenReturn(list);
		when(appointmentDto.convertAppointmentIntoDto(list)).thenReturn(dtoList);

		List<AppointmentDto> result = appointmentService.getAppointmentForPatient("jane");

		assertEquals(1, result.size());
	}

	@Test
	public void getAppointmentsForDoctor_Test() {
		List<Appointment> list = List.of(appointment);
		List<AppointmentDto> dtoList = List.of(new AppointmentDto());

		when(doctorRepository.getDoctorByUsername("drjohn")).thenReturn(doctor);
		when(appointmentRepository.findByDoctor(doctor)).thenReturn(list);
		when(appointmentDto.convertAppointmentIntoDto(list)).thenReturn(dtoList);

		List<AppointmentDto> result = appointmentService.getAppointmentForDoctor("drjohn");

		assertEquals(1, result.size());
	}

	@Test
	public void rescheduleAppointment_Valid_Test() {
		Appointment updated = new Appointment();
		updated.setScheduledDate(LocalDate.now().plusDays(2));
		updated.setScheduledTime(LocalTime.of(15, 0));
		updated.setStatus(Appointment.Status.RESCHEDULED);

		when(appointmentRepository.findById(100)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

		Appointment result = appointmentService.rescheduleAppointment(100, updated);

		assertEquals(Appointment.Status.RESCHEDULED, result.getStatus());
	}

	@Test
	public void rescheduleAppointment_NotFound_Test() {
		when(appointmentRepository.findById(999)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			appointmentService.rescheduleAppointment(999, new Appointment());
		});

		assertEquals("Appointment not found", ex.getMessage());
	}

	@Test
	public void getAllAppointments_Test() {
		List<Appointment> list = List.of(appointment);
		List<AppointmentDto> dtoList = List.of(new AppointmentDto());

		when(appointmentRepository.findAll()).thenReturn(list);
		when(appointmentDto.convertAppointmentIntoDto(list)).thenReturn(dtoList);

		List<AppointmentDto> result = appointmentService.getAllAppointment();

		assertEquals(1, result.size());
	}
	
	@AfterEach
	public void afterTest() {
		appointment=null;
		slot=null;
		patient=null;
		doctor=null;
		System.out.println("Test data cleared.");
	}
}
