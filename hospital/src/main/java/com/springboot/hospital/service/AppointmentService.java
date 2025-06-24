package com.springboot.hospital.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	public Appointment bookAppointment(String username, int slotId, Appointment appointment) {
		Patient patient = patientRepository.getPatientByUsername(username);
		DoctorSlot slot = doctorSlotRepository.findById(slotId)
				.orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

		if (slot.getBookedCount() >= slot.getMaxAppointment()) {
			throw new ResourceNotFoundException("Slot is fully booked.");
		}

		appointment.setPatient(patient);
		appointment.setDoctor(slot.getDoctor());
		appointment.setDoctorSlot(slot);
		appointment.setScheduledDate(slot.getDate());
		appointment.setScheduledTime(slot.getTime());
		appointment.setStatus(Appointment.Status.SCHEDULED);

		slot.setBookedCount(slot.getBookedCount() + 1);
		doctorSlotRepository.save(slot);

		return appointmentRepository.save(appointment);
	}

	public List<AppointmentDto> getAppointmentForPatient(String username) {
		Patient patient = patientRepository.getPatientByUsername(username);
		List<Appointment> list = appointmentRepository.findByPatient(patient);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public List<AppointmentDto> getAppointmentForDoctor(String username) {
		Doctor doctor = doctorRepository.getDoctorByUsername(username);
		List<Appointment> list = appointmentRepository.findByDoctor(doctor);
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public Appointment rescheduleAppointment(int id, Appointment updated) {
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
		List<Appointment> list = appointmentRepository.findAll();
		return appointmentDto.convertAppointmentIntoDto(list);
	}

	public AppointmentDto getById(int id) {
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

	public Appointment getLastAppointmentByPatientId(int patientId) {
	    return appointmentRepository.findLastAppointmentByPatientId(patientId)
	           .orElseThrow(() -> new RuntimeException("No appointments found for patient"));
	}

	public Map<LocalDate, Long> getAppointmentCountByDate(String username) {
		LocalDate startDate=LocalDate.now().minusDays(6); //7 days including today
		List<Object[]> results=appointmentRepository.countAppointmentByDate(username, startDate);
		Map<LocalDate, Long> countMap =new LinkedHashMap<>();
		for(Object[] row : results) {
			LocalDate date=(LocalDate) row[0];
			Long count=(Long) row[1];
			countMap.put(date, count);
			}
		return countMap;
		}
		

	public void deleteAppointment(int id) {
	    Appointment appointment = appointmentRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
	    appointmentRepository.delete(appointment);
	}
	

}
