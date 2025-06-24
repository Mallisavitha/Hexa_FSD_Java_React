package com.example.coding.service;

import org.springframework.stereotype.Service;

import com.example.coding.exception.ResourceNotFoundException;
import com.example.coding.model.Appointment;
import com.example.coding.model.DoctorSlot;
import com.example.coding.model.Patient;
import com.example.coding.repository.AppointmentRepository;
import com.example.coding.repository.DoctorRepository;
import com.example.coding.repository.DoctorSlotRepository;
import com.example.coding.repository.PatientRepository;


@Service
public class AppointmentService {
	
	private AppointmentRepository appointmentRepository;
	private PatientRepository patientRepository;
	private DoctorRepository doctorRepository;
	private DoctorSlotRepository doctorSlotRepository;
	
	public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository,
			DoctorRepository doctorRepository, DoctorSlotRepository doctorSlotRepository) {
		super();
		this.appointmentRepository = appointmentRepository;
		this.patientRepository = patientRepository;
		this.doctorRepository = doctorRepository;
		this.doctorSlotRepository = doctorSlotRepository;
	}

	public Appointment bookAppointment(String username, int slotId, Appointment input) {
		Patient patient = patientRepository.getPatientByUsername(username);
	    DoctorSlot slot = doctorSlotRepository.findById(slotId)
	            .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

	    if (slot.getCount() >= slot.getMaxAppointment()) {
	        throw new ResourceNotFoundException("Slot is fully booked");
	    }
	    
	    Appointment appointment=new Appointment();
	    appointment.setPatient(patient);
	    appointment.setDoctor(slot.getDoctor());
	    appointment.setDoctorSlot(slot);
	    appointment.setAppointmentDate(slot.getDate());
	    appointment.setAppointmentTime(slot.getTime());
	    
	    slot.setCount(slot.getCount() + 1);
		doctorSlotRepository.save(slot);

		return appointmentRepository.save(appointment);
	}


}
