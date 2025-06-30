package com.springboot.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // <- This ensures that this class gets called during every API call
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf((csrf) -> csrf.disable())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/api/user/signup").permitAll()
						.requestMatchers("/api/user/token").authenticated()
						.requestMatchers("/api/user/details").authenticated()
						
						//Department
						.requestMatchers("/api/department/get-all").permitAll()
						.requestMatchers("/api/department/delete/{id}").hasAuthority("RECEPTIONIST")
						
						//TestRecommendation
						.requestMatchers("/api/test/recommend/{consultationId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/test/consultation/{consultationId}").hasAnyAuthority("DOCTOR","PATIENT")
						.requestMatchers("/api/test/update/{testId}").hasAuthority("LABSTAFF")
						.requestMatchers("/api/test/all").hasAuthority("LABSTAFF")
						.requestMatchers("/api/test/upload/report/{testId}").hasAuthority("LABSTAFF")
						.requestMatchers("/api/test/delete/report/{testId}").hasAuthority("LABSTAFF")
						
						//Pescription
						.requestMatchers("/api/prescription/add/{consultationId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/prescription/get/{consultationId}").hasAnyAuthority("PATIENT","DOCTOR")
						.requestMatchers("/api/prescription/update/{consultationId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/prescription/delete/{prescriptionId}").hasAuthority("DOCTOR")
						
						//Consutatiom
						.requestMatchers("/api/consultation/add/{appointmentId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/consultation/doctor/get/{appointmentId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/consultation/update/{appointmentId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/consultation/patient/get/{appointmentId}").hasAuthority("PATIENT")
						.requestMatchers("/api/consultation/get-all").hasAuthority("RECEPTIONIST")
						
						//Appointment
						.requestMatchers("/api/appointment/book/{doctorId}").hasAuthority("PATIENT")
						.requestMatchers("/api/appointment/own").hasAuthority("PATIENT")
						.requestMatchers("/api/appointment/own/date").permitAll()
						.requestMatchers("/api/appointment/reschedule/{id}").permitAll()
						.requestMatchers("/api/appointment/get-all").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/appointment/last/{patientId}").hasAuthority("DOCTOR")		
						.requestMatchers("/api/appointment/last-7-days").permitAll()
						.requestMatchers("/api/appointment/doctor/new").hasAuthority("DOCTOR")
						.requestMatchers("/api/appointment/doctor/upcoming").hasAuthority("DOCTOR")
						.requestMatchers("/api/appointment/doctor/past").hasAuthority("DOCTOR")
						.requestMatchers("/api/appointment/own/past").hasAuthority("PATIENT")
						.requestMatchers("/api/appointment/own/upcoming").hasAuthority("PATIENT")
						
						//Doctor-slot
						.requestMatchers("/api/doctor-slot/add").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor-slot/my-slot").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor-slot/all").permitAll()
						.requestMatchers("api/doctor-slot/doctor-name/{name}").permitAll()
						.requestMatchers("/api/doctor-slot/delete/{slotId}").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor-slot/by-doctor/{doctorId}").permitAll()
						
						//Receptionist
						.requestMatchers("/api/receptionist/add").permitAll()
						.requestMatchers("/api/receptionist/get-one").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/receptionist/get-all").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/receptionist/update").hasAuthority("RECEPTIONIST")
						
						//Doctor
						.requestMatchers("/api/doctor/add/{deptId}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/doctor/get-one").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor/upload/profile-pic").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor/upload/profile-pic/{id}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/doctor/get-all").permitAll()
						.requestMatchers("/api/doctor/delete/{id}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/doctor/update").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor/search/{name}").permitAll()
						.requestMatchers("/api/doctor/specialization/{specialization}").hasAnyAuthority("PATIENT","RECEPTIONIST")
						
						//Lab-Staff
						.requestMatchers("/api/labstaff/add/{departmentId}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/labstaff/get-one").hasAuthority("LABSTAFF")
						.requestMatchers("/api/labstaff/update").hasAuthority("LABSTAFF")
						.requestMatchers("/api/labstaff/get-all").hasAuthority("RECEPTIONIST")

						//Patient
						.requestMatchers("/api/patient/add").permitAll()
						.requestMatchers("/api/patient/get-one").permitAll()
						.requestMatchers("/api/patient/update").hasAuthority("PATIENT")
						.requestMatchers("/api/author/upload/profile-pic/{id}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/patient/get-all").hasAnyAuthority("RECEPTIONIST","DOCTOR")
						.requestMatchers("/api/patient/specialization/{specialization}").hasAnyAuthority("DOCTOR","RECEPTIONIST")
						.requestMatchers("/api/patient/date/{date}").hasAnyAuthority("DOCTOR","RECEPTIONIST")
						.requestMatchers("/api/patient/search/{name}").hasAnyAuthority("DOCTOR","RECEPTIONIST")
						.requestMatchers("/api/patient/upload/profile-pic/{id}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/patient/upload/profile-pic").hasAuthority("PATIENT")
						
						.anyRequest().authenticated())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults()); // <- this activated http basic techniques
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager getAuthManagerr(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}

}
