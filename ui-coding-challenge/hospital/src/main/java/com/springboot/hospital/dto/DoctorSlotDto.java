package com.springboot.hospital.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.DoctorSlot;

@Component
public class DoctorSlotDto {
	
	private int slotId;
	private LocalDate date;
	private LocalTime time;
	private int maxAppointment;
	private int bookedCount;
	private String doctorName;
	
	public int getSlotId() {
		return slotId;
	}
	
	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public int getMaxAppointment() {
		return maxAppointment;
	}
	
	public void setMaxAppointment(int maxAppointment) {
		this.maxAppointment = maxAppointment;
	}
	
	public int getBookedCount() {
		return bookedCount;
	}
	
	public void setBookedCount(int bookedCount) {
		this.bookedCount = bookedCount;
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public List<DoctorSlotDto> convertSlotIntoDto(List<DoctorSlot> list){
		List<DoctorSlotDto> listDto = new ArrayList<>();
		list.stream().forEach(slot -> {
			DoctorSlotDto dto=new DoctorSlotDto();
			dto.setSlotId(slot.getSlotId());
			dto.setDate(slot.getDate());
			dto.setTime(slot.getTime());
			dto.setMaxAppointment(slot.getMaxAppointment());
			dto.setBookedCount(slot.getBookedCount());
			dto.setDoctorName(slot.getDoctor().getFullName());
			listDto.add(dto);
		});
		return listDto;
	}
	
	
	

}
