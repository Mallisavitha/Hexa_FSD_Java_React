package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.DoctorSlot;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Integer> {

	@Query("select s from DoctorSlot s where s.doctor.doctorId=?1")
	List<DoctorSlot> findByDoctorId(int doctorId);

	@Query("select s from DoctorSlot s where s.doctor.fullName=?1")
	List<DoctorSlot> getSlotByDoctorName(String name);

}
