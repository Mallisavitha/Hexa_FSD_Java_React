package com.springboot.hospital.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.hospital.model.DoctorSlot;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Integer> {

	 @Query("SELECT ds FROM DoctorSlot ds WHERE ds.doctor.user.username = :username")
	 List<DoctorSlot> findSlotsByDoctorUsername(@Param("username") String username);
	
	@Query("select s from DoctorSlot s where s.doctor.fullName=?1")
	List<DoctorSlot> getSlotByDoctorName(String name);

	@Query("SELECT s FROM DoctorSlot s WHERE s.doctor.doctorId = :doctorId AND (s.date > :date OR (s.date = :date AND s.time >= :time))")
	List<DoctorSlot> getAvailableSlotsByDoctor(@Param("doctorId") int doctorId, @Param("date") LocalDate date,
			@Param("time") LocalTime time);

}
