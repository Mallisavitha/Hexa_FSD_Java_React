package com.springboot.hospital.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.hospital.model.Department;
import com.springboot.hospital.service.DepartmentService;

@RestController
@RequestMapping("/api/department")
@CrossOrigin(origins = "http://localhost:5173")
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	Logger logger=LoggerFactory.getLogger("DepartmentController");
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody Department department){
		logger.info("Add Department");
		return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.addDepartment(department));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		logger.info("Fetching all departments");
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getAllDepartment());
	}
	
	@GetMapping("get-one/{id}")
	public ResponseEntity<?> getById(@PathVariable int id){
		logger.info("Get Department by ID");
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getById(id));
	}
	
	//get department by name
	@GetMapping("search/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name){
		logger.info("Get Department by name");
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getByName(name));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable int id,@RequestBody Department updated){
		logger.info("Department is updating");
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.updateDepartment(id, updated));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id){
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted");
	}

}
