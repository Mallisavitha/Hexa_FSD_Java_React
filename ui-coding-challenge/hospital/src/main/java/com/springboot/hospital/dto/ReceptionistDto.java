package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.Receptionist;

@Component
public class ReceptionistDto {
	
	private int adminId;
	private String name;
	private String email;
	private String phone;
	
	public int getAdminId() {
		return adminId;
	}
	
	public void setAdminId(int adminId) {
		this.adminId = adminId;
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<ReceptionistDto> convertReceptionistIntoDto(List<Receptionist> list){
		List<ReceptionistDto> listDto=new ArrayList<>();
		list.stream().forEach(receptionist -> {
			ReceptionistDto dto=new ReceptionistDto();
			dto.setAdminId(receptionist.getAdminId());
			dto.setName(receptionist.getName());
			dto.setEmail(receptionist.getEmail());
			dto.setPhone(receptionist.getEmail());
			listDto.add(dto);
		});		
		return listDto;
	}
	
	

}
