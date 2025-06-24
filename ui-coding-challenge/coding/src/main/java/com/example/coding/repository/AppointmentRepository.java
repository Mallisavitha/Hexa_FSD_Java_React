package com.example.coding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.coding.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {



}
