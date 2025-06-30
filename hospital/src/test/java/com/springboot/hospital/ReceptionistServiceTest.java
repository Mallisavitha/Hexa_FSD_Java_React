package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.ReceptionistDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.ReceptionistRepository;
import com.springboot.hospital.service.ReceptionistService;
import com.springboot.hospital.service.UserService;

@SpringBootTest
public class ReceptionistServiceTest {

	@InjectMocks
	private ReceptionistService receptionistService;

	@Mock
	private ReceptionistRepository receptionistRepository;

	@Mock
	private UserService userService;

	@Mock
	private ReceptionistDto receptionistDto;

	private Receptionist receptionist;
	private User user;

	@BeforeEach
	public void init() {
		user = new User();
		user.setId(1);
		user.setUsername("Priya");
		user.setPassword("priya@123");

		receptionist = new Receptionist();
		receptionist.setAdminId(1);
		receptionist.setName("Sita");
		receptionist.setEmail("sita@gmail.com");
		receptionist.setPhone("9638527418");
		receptionist.setUser(user);
	}

	@Test
	public void insertReceptionstTest() {
		user.setRole(null);
		when(userService.signUp(any(User.class))).thenReturn(user);
		when(receptionistRepository.save(any(Receptionist.class))).thenReturn(receptionist);

		Receptionist saved = receptionistService.insertReceptionist(receptionist);

		assertNotNull(saved);
		assertEquals("RECEPTIONIST", saved.getUser().getRole());
	}

	@Test
	public void getAllReceptionistsTest() {
		List<Receptionist> receptionists = List.of(receptionist);
		List<ReceptionistDto> expectedDtoList = List.of(new ReceptionistDto());

		when(receptionistRepository.findAll()).thenReturn(receptionists);
		when(receptionistDto.convertReceptionistIntoDto(receptionists)).thenReturn(expectedDtoList);

		List<ReceptionistDto> result = receptionistService.getAll();

		assertEquals(1, result.size());
	}

	@Test
	public void getById_ValidId_Test() {
		when(receptionistRepository.findById(1)).thenReturn(Optional.of(receptionist));

		Receptionist result = receptionistService.getById(1);

		assertNotNull(result);
		assertEquals("Sita", result.getName());
	}

	@Test
	public void getById_InvalidId_Test() {
		when(receptionistRepository.findById(anyInt())).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			receptionistService.getById(999);
		});

		assertEquals("Invalid Id".toLowerCase(), ex.getMessage().toLowerCase());
	}

	@Test
	public void updateReceptionistTest() {
		Receptionist updated = new Receptionist();
		updated.setName("Sita Devi");
		updated.setEmail("newemail@mail.com");
		updated.setPhone("9123456780");

		when(receptionistRepository.getReceptionistByUsername("reception1")).thenReturn(receptionist);
		when(receptionistRepository.save(any(Receptionist.class))).thenReturn(receptionist);

		Receptionist result = receptionistService.update("reception1", updated);

		assertEquals("Sita Devi", result.getName());
		assertEquals("newemail@mail.com", result.getEmail());
		assertEquals("9123456780", result.getPhone());
	}

	@Test
	public void deleteReceptionistTest() {
		when(receptionistRepository.getReceptionistByUsername("reception1")).thenReturn(receptionist);

		receptionistService.delete("reception1");

		// no assertion needed, just ensure no exception
	}

	@Test
	public void getReceptionistByUsernameTest() {
		when(receptionistRepository.getReceptionistByUsername("reception1")).thenReturn(receptionist);

		Receptionist result = receptionistService.getReceptionistByUsername("reception1");

		assertEquals("Sita", result.getName());
	}

	@AfterEach
	public void cleanup() {
		receptionist = null;
		user = null;
	}
}
