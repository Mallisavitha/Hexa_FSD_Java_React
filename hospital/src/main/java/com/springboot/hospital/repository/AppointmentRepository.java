package com.springboot.hospital.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{

	@Query("select a from Appointment a where a.patient.patientId=?1")
	List<Appointment> getByPatientId(int patientId);
	
	@Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = ?1")
    List<Appointment> getByDoctorId(int doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.receptionist.adminId = ?1")
    List<Appointment> getByReceptionistId(int receptionistId);
    
    @Query("SELECT a FROM Appointment a WHERE a.status = ?1")
    List<Appointment> findByStatus(Appointment.Status status);

}
