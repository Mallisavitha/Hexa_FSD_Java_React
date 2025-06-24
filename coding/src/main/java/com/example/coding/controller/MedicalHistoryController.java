package com.example.coding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coding.dto.MedicalHistoryDTO;
import com.example.coding.service.MedicalHistoryService;

//@RestController
//@RequestMapping("api/medical")
public class MedicalHistoryController {
	
	@Autowired
	private MedicalHistoryService medicalHistoryService;

//	 @GetMapping("/get/{patientId}")
//	    public List<MedicalHistoryDTO> getPatientWithMedicalHistory(@PathVariable int patientId) {
//	         
//	        return medicalHistoryService.getPatientWithHistory(patientId);
//	    }
}
