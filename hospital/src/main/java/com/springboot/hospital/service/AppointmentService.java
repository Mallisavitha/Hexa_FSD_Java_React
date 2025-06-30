package com.springboot.hospital.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepository;
	private PatientRepository patientRepository;
	private AppointmentDto appointmentDto;
	private DoctorRepository doctorRepository;
	private DoctorSlotRepository doctorSlotRepository;
	
	Logger logger = LoggerFactory.getLogger(AppointmentService.class);

	public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			AppointmentDto appointmentDto, DoctorRepository doctorRepository,
			DoctorSlotRepository doctorSlotRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.appointmentDto = appointmentDto;
		this.doctorRepository = doctorRepository;
		this.doctorSlotRepository = doctorSlotRepository;
	}

	public Appointment bookAppointment(String username, int doctorId, AppointmentDto dto) {
		// Fetch patient
		Patient patient = patientRepository.getPatientByUsername(username);

		// Fetch doctor or throw if not found
		Doctor doctor = doctorRepository.findById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

		// Fetch slot or throw if not found
		DoctorSlot slot = doctorSlotRepository.findById(dto.getDoctorSlotId())
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		// Create new appointment
		Appointment appointment = new Appointment();
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);
		appointment.setDoctorSlot(slot); //  FIX HERE
		appointment.setScheduledDate(slot.getDate());
		appointment.setScheduledTime(slot.getTime());
		appointment.setNatureOfVisit(dto.getNatureOfVisit());
		appointment.setDescription(dto.getDescription());
		appointment.setStatus(appointment.getStatus().SCHEDULED); // assuming this is an enum

		// Update slot's booked count
		slot.setBookedCount(slot.getBookedCount() + 1);
		doctorSlotRepository.save(slot);

		// Save and return appointment
		logger.info("Appointment booked successfully for patient: {}",patient.getFullName());
		return appointmentRepository.save(appointment);
	}


	public List<AppointmentDto> getUpcomingAppointmentsForPatient(String username) {
		logger.info("Fetching upcoming appointments for patient username: {}",username);
		Patient patient = patientRepository.getPatientByUsername(username);
		LocalDate today = LocalDate.now();
		List<Appointment> upcoming = appointmentRepository.findUpcomingAppointments(patient, today);
		return appointmentDto.convertAppointmentIntoDto(upcoming);
	}

	public List<AppointmentDto> getPastAppointmentsForPatient(String username, int page, int size) {
		logger.info("Fetching past appointments for username: {}",username);
		Patient patient = patientRepository.getPatientByUsername(username);
		Pageable pageable = PageRequest.of(page, size, Sort.by("scheduledDate").descending());
		LocalDate today = LocalDate.now();
		List<Appointment> past = appointmentRepository.findPastAppointments(patient, today, pageable);
		return appointmentDto.convertAppointmentIntoDto(past);
	}

	public Appointment rescheduleAppointment(int id, Appointment updated) {
		logger.info("Rescheuling aappointment for ID: {}",id);
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		if (updated.getScheduledDate() != null)
			appointment.setScheduledDate(updated.getScheduledDate());

		if (updated.getScheduledTime() != null)
			appointment.setScheduledTime(updated.getScheduledTime());

		appointment.setStatus(Appointment.Status.RESCHEDULED);

		return appointmentRepository.save(appointment);
	}

	public List<AppointmentDto> getAllAppointment() {
		logger.info("Fetching all appointments");
		List<Appointment> list = appointmentRepository.findAll();
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public AppointmentDto getById(int id) {
		logger.info("Fetching appointment by ID : {}",id);
		Appointment appt = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));

		AppointmentDto dto = new AppointmentDto();
		dto.setAppointmentId(appt.getAppointmentId());
		dto.setScheduledDate(appt.getScheduledDate());
		dto.setScheduledTime(appt.getScheduledTime());
		dto.setNatureOfVisit(appt.getNatureOfVisit());
		dto.setDescription(appt.getDescription());
		dto.setStatus(appt.getStatus().name());
		dto.setPatientName(appt.getPatient().getFullName()); // assuming mapping exists

		return dto;
	}

	public void deleteAppointment(int id) {
		logger.info("This appointment is deleting ID: {}",id);
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
		appointmentRepository.delete(appointment);

	}

	public List<Map<String, Object>> getLast7DaysAppointments() {
		logger.info("Fetching appointment counts for 7 days");
		LocalDate endDate = LocalDate.now();
		LocalDate startDate = endDate.minusDays(6); // Last 7 days including today

		List<Object[]> results = appointmentRepository.findAppointmentCountsByDateRange(startDate, endDate);

		// Create a map from scheduledDate to count
		Map<LocalDate, Long> countMap = new HashMap<>();
		for (Object[] row : results) {
			LocalDate date = (LocalDate) row[0];
			Long count = (Long) row[1];
			countMap.put(date, count);
		}

		// Fill in all 7 days (even with 0 if missing)
		List<Map<String, Object>> response = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			LocalDate date = startDate.plusDays(i); // each day from start to end
			Map<String, Object> entry = new HashMap<>();
			entry.put("date", date.toString()); // convert date to string
			entry.put("count", countMap.getOrDefault(date, 0L)); // get count or 0
			response.add(entry);
		}

		return response;
	}

	public List<AppointmentDto> getAppointmentsByDate(String username, LocalDate date) {
		logger.info("Fetching appointment for this date: {}",date);
		List<Appointment> list = appointmentRepository.findByPatientUsernameAndDate(username, date);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public List<AppointmentDto> getTodayAppointmentsForDoctor(String username) {
		logger.info("Fetching todays appointments for doctor: {}",username);
		Doctor doctor = doctorRepository.getDoctorByUsername(username);
		List<Appointment> list = appointmentRepository.findTodayAppointments(LocalDate.now(), doctor);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public List<AppointmentDto> getUpcomingAppointmentsForDoctor(String username) {
		logger.info("Fetching upcoming appointments for doctor: {}",username);
		Doctor doctor = doctorRepository.getDoctorByUsername(username);
		List<Appointment> list = appointmentRepository.findUpcomingAppointments(LocalDate.now(), doctor);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public List<AppointmentDto> getPastAppointmentsForDoctor(String username) {
		logger.info("Fetching past appointments for dotor: {}",username);
		Doctor doctor = doctorRepository.getDoctorByUsername(username);
		List<Appointment> list = appointmentRepository.findPastAppointments(LocalDate.now(), doctor);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

}
