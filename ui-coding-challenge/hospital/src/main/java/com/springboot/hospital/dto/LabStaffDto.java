package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.LabStaff;

@Component
public class LabStaffDto {
	
	private int labStaffId;
	private String name;
	private String email;
	private String deptName;
	
	public int getLabStaffId() {
		return labStaffId;
	}
	
	public void setLabStaffId(int labStaffId) {
		this.labStaffId = labStaffId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDeptName() {
		return deptName;
	}
	
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public List<LabStaffDto> convertLabIntoDto(List<LabStaff> list){
		List<LabStaffDto> listDto=new ArrayList<>();
		list.stream().forEach(lab -> {
			LabStaffDto dto=new LabStaffDto();
			dto.setLabStaffId(lab.getLabStaffId());
			dto.setName(lab.getName());
			dto.setEmail(lab.getEmail());
			dto.setDeptName(lab.getDepartment().getName());
			listDto.add(dto);
		});
		return listDto;
	}
}
