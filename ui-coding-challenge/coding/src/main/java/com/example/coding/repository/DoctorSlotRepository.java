package com.example.coding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coding.model.DoctorSlot;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Integer> {

}
