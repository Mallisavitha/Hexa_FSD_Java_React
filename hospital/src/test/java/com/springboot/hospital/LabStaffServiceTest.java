package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.dto.LabStaffDto;
import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.model.LabStaff;
import com.springboot.hospital.model.Receptionist;
import com.springboot.hospital.model.User;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.repository.LabStaffRepository;
import com.springboot.hospital.repository.ReceptionistRepository;
import com.springboot.hospital.service.LabStaffService;
import com.springboot.hospital.service.UserService;

@SpringBootTest
public class LabStaffServiceTest {

	@InjectMocks
	private LabStaffService labStaffService;

	@Mock
	private LabStaffRepository labRepository;

	@Mock
	private DepartmentRepository departmentRepository;

	@Mock
	private ReceptionistRepository receptionistRepository;

	@Mock
	private UserService userService;

	@Mock
	private LabStaffDto labStaffDto;

	private LabStaff labStaff;
	private Department department;
	private Receptionist receptionist;
	private User user;

	@BeforeEach
	public void setUp() {
		user = new User();
		user.setUsername("labuser");
		user.setRole("LABSTAFF");

		department = new Department();
		department.setDepartmentId(1);
		department.setName("Pathology");

		receptionist = new Receptionist();
		receptionist.setAdminId(2);
		receptionist.setName("Reshma");

		labStaff = new LabStaff();
		labStaff.setLabStaffId(1);
		labStaff.setName("John");
		labStaff.setEmail("john@lab.com");
		labStaff.setUser(user);
		labStaff.setDepartment(department);
		labStaff.setReceptionist(receptionist);
	}

	@Test
	public void addLabStaffSuccessTest() {
		when(userService.signUp(any(User.class))).thenReturn(user);
		when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
		when(receptionistRepository.getReceptionistByUsername("res123")).thenReturn(receptionist);
		when(labRepository.save(any(LabStaff.class))).thenReturn(labStaff);

		LabStaff saved = labStaffService.addLabStaff(1, labStaff, "res123");

		assertNotNull(saved);
		assertEquals("John", saved.getName());
	}

	@Test
	public void addLabStaffDepartmentNotFoundTest() {
		when(departmentRepository.findById(99)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
				() -> labStaffService.addLabStaff(99, labStaff, "res123"));

		assertTrue(ex.getMessage().toLowerCase().contains("invalid department id"));
	}

	@Test
	public void getAllTest() {
		when(labRepository.findAll()).thenReturn(List.of(labStaff));
		when(labStaffDto.convertLabIntoDto(anyList())).thenReturn(List.of(new LabStaffDto()));

		List<LabStaffDto> list = labStaffService.getAll();
		assertEquals(1, list.size());
	}

	@Test
	public void getByIdSuccessTest() {
		when(labRepository.findById(1)).thenReturn(Optional.of(labStaff));

		LabStaff found = labStaffService.getById(1);
		assertEquals("John", found.getName());
	}

	@Test
	public void getByIdNotFoundTest() {
		when(labRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> labStaffService.getById(99));
	}

	@Test
	public void deleteSuccessTest() {
		when(labRepository.getLabstaffByUsername("labuser")).thenReturn(labStaff);

		assertDoesNotThrow(() -> labStaffService.delete("labuser"));
	}

	@Test
	public void getLabstaffByUsernameTest() {
		when(labRepository.getLabstaffByUsername("labuser")).thenReturn(labStaff);

		LabStaff found = labStaffService.getLabstaffByUsername("labuser");
		assertEquals("John", found.getName());
	}

	@Test
	public void updateLabStaffSuccessTest() {
		when(labRepository.getLabstaffByUsername("labuser")).thenReturn(labStaff);
		when(labRepository.save(any(LabStaff.class))).thenReturn(labStaff);

		LabStaff updated = new LabStaff();
		updated.setName("Johnny");
		updated.setEmail("johnny@lab.com");

		LabStaff result = labStaffService.updateLabStaff("labuser", updated);

		assertEquals("Johnny", result.getName());
		assertEquals("johnny@lab.com", result.getEmail());
	}
}
