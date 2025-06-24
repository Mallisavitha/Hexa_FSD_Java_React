package com.example.coding.service;

import org.springframework.stereotype.Service;

import com.example.coding.model.Doctor;
import com.example.coding.model.User;
import com.example.coding.repository.DoctorRepository;

@Service
public class DoctorService {

	 private DoctorRepository doctorRepository;
	    private UserService userService;
	    
	    

	    public DoctorService(DoctorRepository doctorRepository, UserService userService) {
			super();
			this.doctorRepository = doctorRepository;
			this.userService = userService;
		}



		public Doctor addDoctor(Doctor doctor) {
	    	// Take user from the Learner Object
			User user = doctor.getUser();
			
			// Set Role as LEARNER in User
			user.setRole("DOCTOR");
			
			// save user in DB
			user = userService.signUp(user);
			
			// attach User in Learner
			doctor.setUser(user);
	    	
	        return doctorRepository.save(doctor);
	    }
}
