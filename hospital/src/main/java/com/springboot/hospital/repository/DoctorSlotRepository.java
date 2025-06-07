package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.hospital.model.DoctorSlot;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Integer> {

	List<DoctorSlot> findByDoctorDoctorId(int doctorId);

}
