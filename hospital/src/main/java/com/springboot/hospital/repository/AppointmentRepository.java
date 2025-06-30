package com.springboot.hospital.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Doctor;
import com.springboot.hospital.model.DoctorSlot;
import com.springboot.hospital.model.Patient;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	@Query("select a from Appointment a where a.patient.user.username=?1")
	List<Appointment> findByPatientUserUsername(String username);

	@Query("select a from Appointment a where a.doctor.user.username=?1")
	List<Appointment> findByDoctorUserUsername(String username);

	@Query("SELECT a FROM Appointment a WHERE a.doctorSlot.slotId = :slotId")
	List<Appointment> getAppointmentsBySlotId(int slotId);

	@Query("SELECT a.scheduledDate, COUNT(a) FROM Appointment a "
			+ "WHERE a.scheduledDate BETWEEN :startDate AND :endDate " + "GROUP BY a.scheduledDate "
			+ "ORDER BY a.scheduledDate")
	List<Object[]> findAppointmentCountsByDateRange(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	@Query("SELECT a FROM Appointment a WHERE a.patient.user.username = ?1 AND a.scheduledDate = :date")
	List<Appointment> findByPatientUsernameAndDate(@Param("username") String username, @Param("date") LocalDate date);
	
	@Query("SELECT a FROM Appointment a WHERE a.scheduledDate = :today AND a.doctor = :doctor")
	List<Appointment> findTodayAppointments(@Param("today") LocalDate today, @Param("doctor") Doctor doctor);

	@Query("SELECT a FROM Appointment a WHERE a.scheduledDate > :today AND a.doctor = :doctor")
	List<Appointment> findUpcomingAppointments(@Param("today") LocalDate today, @Param("doctor") Doctor doctor);

	@Query("SELECT a FROM Appointment a WHERE a.scheduledDate < :today AND a.doctor = :doctor")
	List<Appointment> findPastAppointments(@Param("today") LocalDate today, @Param("doctor") Doctor doctor);
	
	@Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND a.scheduledDate >= :date")
	List<Appointment> findUpcomingAppointments(@Param("patient") Patient patient, @Param("date") LocalDate date);

	@Query("SELECT a FROM Appointment a WHERE a.patient = :patient AND a.scheduledDate < :date")
	List<Appointment> findPastAppointments(@Param("patient") Patient patient, @Param("date") LocalDate date, Pageable pageable);



}
