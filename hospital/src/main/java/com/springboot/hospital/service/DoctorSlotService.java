package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.DoctorSlotRepository;

@Service
public class DoctorSlotService {
	
	private DoctorSlotRepository doctorSlotRepository;
	private DoctorRepository doctorRepository;

	public DoctorSlotService(DoctorSlotRepository doctorSlotRepository, DoctorRepository doctorRepository) {
		super();
		this.doctorSlotRepository = doctorSlotRepository;
		this.doctorRepository = doctorRepository;
	}

	public DoctorSlot addSlot(String username, DoctorSlot slot) {
		Doctor doctor=doctorRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Doctor Not Found"));
		slot.setDoctor(doctor);
		return doctorSlotRepository.save(slot);
	}

	public List<DoctorSlot> getSlotsByDoctorUsername(String username) {
		Doctor doctor = doctorRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
		return doctorSlotRepository.findByDoctorDoctorId(doctor.getDoctorId());
	}

	public List<DoctorSlot> getAllSlots() {
		return doctorSlotRepository.findAll();
	}

}
