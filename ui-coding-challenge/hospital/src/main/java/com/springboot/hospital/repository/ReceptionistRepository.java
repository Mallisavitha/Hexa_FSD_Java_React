package com.springboot.hospital.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.Receptionist;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {

	@Query("select l from Receptionist l where l.user.username=?1")
	Receptionist getReceptionistByUsername(String username);



}
