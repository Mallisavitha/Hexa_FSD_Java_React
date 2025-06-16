package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot.hospital.exception.ResourceNotFoundException;
import com.springboot.hospital.model.Department;
import com.springboot.hospital.repository.DepartmentRepository;
import com.springboot.hospital.service.DepartmentService;

@SpringBootTest
public class DepartmentServiceTest {

	@InjectMocks
	private DepartmentService departmentService;
	@Mock
	private DepartmentRepository departmentRepository;

	private Department department;

	@BeforeEach
	public void init() {
		department = new Department();
		department.setDepartmentId(1);
		department.setName("Cardiology");
	}

	@Test
	public void addDepartmentTest() {
		when(departmentRepository.save(department)).thenReturn(department);

		Department saved = departmentService.addDepartment(department);

		assertNotNull(saved);
		assertEquals("Cardiology", saved.getName());
		verify(departmentRepository, times(1)).save(department);
	}

	@Test
	public void getAllDepartmentTest() {
		Department d1 = new Department();
		d1.setDepartmentId(1);
		d1.setName("Cardiology");

		Department d2 = new Department();
		d2.setDepartmentId(2);
		d2.setName("Neurology");
		when(departmentRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

		List<Department> list = departmentService.getAllDepartment();

		assertEquals(2, list.size());
	}

	@Test
	public void getByIdSuccessTest() {
		when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

		Department result = departmentService.getById(1);

		assertNotNull(result);
		assertEquals("Cardiology", result.getName());
	}

	@Test
	public void getByIdNotFoundTest() {
		when(departmentRepository.findById(2)).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.getById(2);
		});

		assertEquals("Department not found", ex.getMessage());
	}

	@Test
	public void getByNameSuccess() {
		when(departmentRepository.findByName("Cardiology")).thenReturn(Optional.of(department));

		Department result = departmentService.getByName("Cardiology");

		assertEquals("Cardiology", result.getName());
	}

	@Test
	public void getByNameNotFoundTest() {
		when(departmentRepository.findByName("Oncology")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
			departmentService.getByName("Oncology");
		});

		assertEquals("Department not found with name: Oncology", ex.getMessage());
	}

	@Test
	public void updateDepartmentTest() {
		Department updated = new Department();
		updated.setName("UpdatedDept");

		when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
		when(departmentRepository.save(any(Department.class))).thenReturn(updated);

		Department result = departmentService.updateDepartment(1, updated);

		assertEquals("UpdatedDept", result.getName());
	}
	
	@AfterEach
	public void afterTest() {
		department=null;
	}

}
