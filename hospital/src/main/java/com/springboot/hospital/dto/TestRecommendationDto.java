package com.springboot.hospital.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.springboot.hospital.model.TestRecommendation;

@Component
public class TestRecommendationDto {
	
	private int testId;
	private String testName;
	private String status;
	private String reportDownload;
	private String doctorName;
	private String patientName;
	private int consultationId;
	private String labStaffName;
	
	public int getTestId() {
		return testId;
	}
	
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	public String getTestName() {
		return testName;
	}
	
	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getReportDownload() {
		return reportDownload;
	}
	
	public void setReportDownload(String reportDownload) {
		this.reportDownload = reportDownload;
	}
	
	public String getDoctorName() {
		return doctorName;
	}
	
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
	public String getPatientName() {
		return patientName;
	}
	
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	
	public int getConsultationId() {
		return consultationId;
	}
	
	public void setConsultationId(int consultationId) {
		this.consultationId = consultationId;
	}
	
	public String getLabStaffName() {
		return labStaffName;
	}
	
	public void setLabStaffName(String labStaffName) {
		this.labStaffName = labStaffName;
	}
	
	public List<TestRecommendationDto> convertTestIntoDto(List<TestRecommendation> list){
		List<TestRecommendationDto> listDto=new ArrayList<>();
		list.stream().forEach(test -> {
			TestRecommendationDto dto=new TestRecommendationDto();
			dto.setTestId(test.getTestId());
			dto.setTestName(test.getTestName());
			dto.setStatus(test.getStatus().name());
			dto.setReportDownload(test.getReportDownload());
			dto.setDoctorName(test.getConsultation().getAppointment().getDoctor().getFullName());
			dto.setPatientName(test.getConsultation().getAppointment().getPatient().getFullName());
			dto.setConsultationId(test.getConsultation().getConsultationId());
			dto.setLabStaffName(test.getLabStaff().getName());
			listDto.add(dto);
		});
		return listDto;
	}
	
	

}
