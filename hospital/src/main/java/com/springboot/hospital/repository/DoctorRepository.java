package com.springboot.hospital.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{

	@Query("SELECT d FROM Doctor d WHERE d.specialization = ?1")
    List<Doctor> getBySpecialization(String specialization);

	@Query("select l from Doctor l where l.user.username=?1")
	Doctor getDoctorByUsername(String username);

	@Query("select d from Doctor d where d.fullName =?1")
	List<Doctor> searchByName(String name);

	Optional<Doctor> findByUserUsername(String username);

	

}
