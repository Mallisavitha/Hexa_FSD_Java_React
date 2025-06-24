package com.example.coding;

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
						.requestMatchers("/api/user/details").authenticated()
						.requestMatchers("/api/doctor/add").permitAll()
						.requestMatchers("/api/patient/add").permitAll()
						.requestMatchers("/api/appointment/book/{slotId}").permitAll()
						.requestMatchers("/api/patient/doctor/{doctorId}").hasAuthority("DOCTOR")
//						.requestMatchers("/api/medical/patient/{patientId}").authenticated()
						.requestMatchers("/api/patient/speciality").permitAll()
						
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
