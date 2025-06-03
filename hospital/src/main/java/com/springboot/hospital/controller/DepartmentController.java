package com.springboot.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class DepartmentController {
	
	@Autowired
	private DepartmentService departmentService;
	
	@PostMapping("/add")
	public ResponseEntity<?> add(@RequestBody Department department){
		return ResponseEntity.status(HttpStatus.CREATED).body(departmentService.addDepartment(department));
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getAllDepartment());
	}
	
	@GetMapping("get-one/{id}")
	public ResponseEntity<?> getById(@PathVariable int id){
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getById(id));
	}
	
	//get department by name
	@GetMapping("search/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name){
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.getByName(name));
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable int id,@RequestBody Department updated){
		return ResponseEntity.status(HttpStatus.OK).body(departmentService.updateDepartment(id, updated));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable int id){
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted");
	}

}
