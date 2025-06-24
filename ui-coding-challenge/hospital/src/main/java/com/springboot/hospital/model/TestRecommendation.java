package com.springboot.hospital.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="testrecommendation")
public class TestRecommendation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int testId;
	
	@Column(name="test_name")
	private String testName;
	
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private TestStatus status;
	
	public enum TestStatus{
		PENDING,IN_PROGRESS,COMPLETED
	}
	
	@Column(name="report_download")
	private String reportDownload;
	
	@ManyToOne
	private Consultation consultation;
	
	@ManyToOne
	private LabStaff labStaff;

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

	public TestStatus getStatus() {
		return status;
	}

	public void setStatus(TestStatus status) {
		this.status = status;
	}

	public String getReportDownload() {
		return reportDownload;
	}

	public void setReportDownload(String reportDownload) {
		this.reportDownload = reportDownload;
	}

	public Consultation getConsultation() {
		return consultation;
	}

	public void setConsultation(Consultation consultation) {
		this.consultation = consultation;
	}

	public LabStaff getLabStaff() {
		return labStaff;
	}

	public void setLabStaff(LabStaff labStaff) {
		this.labStaff = labStaff;
	}
	
	
	
}
