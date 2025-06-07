package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.model.Patient;
import com.springboot.hospital.repository.AppointmentRepository;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.DoctorSlotRepository;
import com.springboot.hospital.repository.PatientRepository;
import com.springboot.hospital.repository.ReceptionistRepository;

@Service
public class AppointmentService {

	private AppointmentRepository appointmentRepository;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	private ReceptionistRepository receptionistRepository;
	private DoctorSlotRepository doctorSlotRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository, ReceptionistRepository receptionistRepository,
			DoctorSlotRepository doctorSlotRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.receptionistRepository = receptionistRepository;
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

	public List<Appointment> getAppointmentForPatient(String username) {
		return appointmentRepository.findByPatientUserUsername(username);
	}

	public List<Appointment> getAppointmentForDoctor(String username) {
		return appointmentRepository.findByDoctorUserUsername(username);
	}

	public Appointment rescheduleAppointment(int id, Appointment updated) {
		Appointment appointment=appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
		
		if(updated.getScheduledDate() != null)
			appointment.setScheduledDate(updated.getScheduledDate());
		
		if(updated.getScheduledTime() != null)
			appointment.setScheduledTime(updated.getScheduledTime());
		
		if(updated.getStatus() != null)
			appointment.setStatus(updated.getStatus());
		
		return appointmentRepository.save(appointment);
	}

	public List<Appointment> getAllAppointment() {
		return appointmentRepository.findAll();
	}

}
