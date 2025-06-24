package com.springboot.hospital.repository;

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

	@Query("select a from Appointment a where a.patient=?1")
	List<Appointment> findByPatient(Patient patient);
	
	@Query("select a from Appointment a where a.doctor=?1")
	List<Appointment> findByDoctor(Doctor doctor);

	@Query("SELECT a FROM Appointment a WHERE a.patient.patientId = :patientId ORDER BY a.scheduledDate DESC")
	Optional<Appointment> findLastAppointmentByPatientId(int patientId);

	@Query("SELECT a.scheduledDate, COUNT(a) FROM Appointment a " +
		       "WHERE a.doctor.user.username = :username AND a.scheduledDate >= :startDate " +
		       "GROUP BY a.scheduledDate ORDER BY a.scheduledDate")
		List<Object[]> countAppointmentByDate(@Param("username") String username,@Param("startDate") LocalDate startDate);


}
