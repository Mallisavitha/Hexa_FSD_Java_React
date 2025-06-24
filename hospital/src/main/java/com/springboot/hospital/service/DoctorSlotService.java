package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.DoctorSlotDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.repository.DoctorRepository;
import com.springboot.hospital.repository.DoctorSlotRepository;

@Service
public class DoctorSlotService {
	
	private DoctorSlotRepository doctorSlotRepository;
	private DoctorRepository doctorRepository;
	private DoctorSlotDto doctorSlotDto;

	public DoctorSlotService(DoctorSlotRepository doctorSlotRepository, DoctorRepository doctorRepository,
			DoctorSlotDto doctorSlotDto) {
		super();
		this.doctorSlotRepository = doctorSlotRepository;
		this.doctorRepository = doctorRepository;
		this.doctorSlotDto = doctorSlotDto;
	}

	public DoctorSlot addSlot(String username, DoctorSlot slot) {
		Doctor doctor=doctorRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Doctor Not Found"));
		slot.setDoctor(doctor);
		return doctorSlotRepository.save(slot);
	}

	public List<DoctorSlot> getSlotsByDoctorUsername(String username) {
		Doctor doctor = doctorRepository.findByUserUsername(username).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
		return doctorSlotRepository.findByDoctorId(doctor.getDoctorId());
	}

	public List<DoctorSlotDto> getAllSlots() {
		List<DoctorSlot> list=doctorSlotRepository.findAll();
		return doctorSlotDto.convertSlotIntoDto(list);
	}

	public List<DoctorSlotDto> getSlotByDoctorName(String name) {
		List<DoctorSlot> list=doctorSlotRepository.getSlotByDoctorName(name);
		return doctorSlotDto.convertSlotIntoDto(list);
	}

	public void deleteSlotByDoctor(int slotId, String username) {
		Doctor doctor = doctorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        DoctorSlot slot = doctorSlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found"));

        doctorSlotRepository.deleteById(slotId);
	}

}
