package com.springboot.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="labstaff")
public class LabStaff {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labStaffId;
	
	private String name;
	private String email;
	private String contact;
	private String profilePic;
	
	@ManyToOne
	private Department department;
	
	@ManyToOne
	private Receptionist receptionist;
	
	@OneToOne
	private User user;

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

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Receptionist getReceptionist() {
		return receptionist;
	}

	public void setReceptionist(Receptionist receptionist) {
		this.receptionist = receptionist;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
	
	
	
}
