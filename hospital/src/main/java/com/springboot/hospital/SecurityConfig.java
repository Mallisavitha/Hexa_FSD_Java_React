package com.springboot.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/patient/get-all").permitAll()
						.requestMatchers("/api/user/signup").permitAll()
						.requestMatchers("/api/user/token").authenticated()
						
						//Receptionist
						.requestMatchers("/api/receptionist/add").permitAll()
						.requestMatchers("/api/receptionist/get-one").hasAuthority("RECEPTIONIST")
						.requestMatchers("api/receptionist/get-all").hasAuthority("RECEPTIONIST")
						.requestMatchers("api/receptionist/update").hasAuthority("RECEPTIONIST")
						
						//Doctor
						.requestMatchers("/api/doctor/add/{deptId}").hasAuthority("RECEPTIONIST")
						.requestMatchers("/api/doctor/get-one").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor/get-all").hasAuthority("RECEPTIONIST")
						.requestMatchers("api/doctor/update").hasAuthority("DOCTOR")
						.requestMatchers("/api/doctor/search-name/{name}").hasAnyAuthority("PATIENT","RECEPTIONIST")
						.requestMatchers("/api/doctor/specialization/{specialization}").hasAnyAuthority("PATIENT","RECEPTIONIST")
						
						//Lab-Staff
						.requestMatchers("/api/labstaff/add/{departmentId}/{receptionistId}").permitAll()
						.requestMatchers("/api/labstaff/get-one").hasAuthority("LABSTAFF")

						//Patient
						.requestMatchers("/api/patient/add").permitAll()
						.requestMatchers("/api/patient/get-one").hasAuthority("PATIENT")
						.requestMatchers("/api/patient/update").hasAuthority("PATIENT")
						.requestMatchers("api/patient/get-all").permitAll()
						
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
