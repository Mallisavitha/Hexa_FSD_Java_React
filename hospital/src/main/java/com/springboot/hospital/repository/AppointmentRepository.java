package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Appointment;
import com.springboot.hospital.model.Doctor;
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

}
