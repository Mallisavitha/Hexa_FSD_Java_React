package com.springboot.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.hospital.model.LabStaff;

public interface LabStaffRepository extends JpaRepository<LabStaff, Integer>{

	@Query("select l from LabStaff l where l.user.username=?1")
	LabStaff getLabstaffByUsername(String username);

}
