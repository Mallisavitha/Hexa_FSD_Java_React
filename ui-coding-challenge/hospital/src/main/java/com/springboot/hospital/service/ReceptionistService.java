package com.springboot.hospital.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.hospital.dto.ReceptionistDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.ReceptionistRepository;

@Service
public class ReceptionistService {

	private ReceptionistRepository recepRepository;
	private UserService userService;
	private ReceptionistDto receptionistDto;

	public ReceptionistService(ReceptionistRepository recepRepository, UserService userService,
			ReceptionistDto receptionistDto) {
		super();
		this.recepRepository = recepRepository;
		this.userService = userService;
		this.receptionistDto = receptionistDto;
	}

	public Receptionist insertReceptionist(Receptionist receptionist) {
		User user = receptionist.getUser();
		user.setRole("RECEPTIONIST");
		user=userService.signUp(user);
		receptionist.setUser(user);
		return recepRepository.save(receptionist);
	}

	public List<ReceptionistDto> getAll() {
		List<Receptionist> list=recepRepository.findAll();
		return receptionistDto.convertReceptionistIntoDto(list);
	}

	public Receptionist getById(int id) {
		return recepRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid Id"));
	}

	public Receptionist update(String username, Receptionist updated) {
		Receptionist db = getReceptionistByUsername(username);
		if (updated.getName() != null)
			db.setName(updated.getName());
		if (updated.getEmail() != null)
			db.setEmail(updated.getEmail());
		if (updated.getPhone() != null)
			db.setPhone(updated.getPhone());
		return recepRepository.save(db);
	}

	public void delete(String username) {
		Receptionist receptionist=getReceptionistByUsername(username);
		recepRepository.delete(receptionist);
	}

	public Receptionist getReceptionistByUsername(String username) {
		return recepRepository.getReceptionistByUsername(username);
	}

}
