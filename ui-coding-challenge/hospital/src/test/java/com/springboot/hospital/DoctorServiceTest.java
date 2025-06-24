//package com.springboot.hospital;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import com.springboot.hospital.dto.DoctorDto;
//import com.springboot.hospital.exception.ResourceNotFoundException;
//import com.springboot.hospital.model.Department;
//import com.springboot.hospital.model.Doctor;
//import com.springboot.hospital.model.Receptionist;
//import com.springboot.hospital.model.User;
//import com.springboot.hospital.repository.DepartmentRepository;
//import com.springboot.hospital.repository.DoctorRepository;
//import com.springboot.hospital.repository.ReceptionistRepository;
//import com.springboot.hospital.service.DoctorService;
//import com.springboot.hospital.service.UserService;
//
//@SpringBootTest
//public class DoctorServiceTest {
//
//	@InjectMocks
//	private DoctorService doctorService;
//	@Mock
//	private DoctorRepository doctorRepository;
//	@Mock
//	private DepartmentRepository departmentRepository;
//	@Mock
//	private ReceptionistRepository receptionistRepository;
//	@Mock
//	private UserService userService;
//	@Mock
//	private DoctorDto doctorDto;
//
//	private Doctor doctor;
//	private DoctorDto dto;
//	private User user;
//	private Department department;
//	private Receptionist receptionist;
//
//	@BeforeEach
//	public void init() {
//		user = new User();
//		user.setUsername("john");
//		user.setPassword("john@123");
//
//		receptionist = new Receptionist();
//		receptionist.setAdminId(10);
//
//		department = new Department();
//		department.setDepartmentId(0);
//		department.setName("Cardiology");
//
//		doctor = new Doctor();
//		doctor.setDoctorId(1);
//		doctor.setFullName("John");
//		doctor.setSpecialization("Cardiology");
//		doctor.setExperienceYear(10);
//		doctor.setUser(user);
//		doctor.setDepartment(department);
//		doctor.setReceptionist(receptionist);
//
//		System.out.println("Doctor object created: " + doctor);
//
//	}
//
//	@Test
//	public void insertDoctorTest() {
//		when(userService.signUp(user)).thenReturn(user);
//		when(departmentRepository.findById(5)).thenReturn(Optional.of(department));
//		when(receptionistRepository.getReceptionistByUsername("recept1")).thenReturn(receptionist);
//		when(doctorRepository.save(any())).thenReturn(doctor);
//
//		Doctor saved = doctorService.insertDoctor(5, doctor, "recept1");
//
//		assertNotNull(saved);
//		assertEquals("John", saved.getFullName());
//		verify(userService).signUp(user);
//		verify(doctorRepository).save(any());
//	}
//
//	@Test
//	public void getAllDoctorTest() {
//		List<Doctor> doctorList = List.of(doctor);
//		Page<Doctor> doctorPage = new PageImpl<>(doctorList);
//
//		when(doctorRepository.findAll(any(Pageable.class))).thenReturn(doctorPage);
//		when(doctorDto.convertDoctorIntoDto(doctorList)).thenReturn(List.of(dto));
//
//		List<?> result = doctorService.getAllDoctors(0, 5);
//
//		assertEquals(1, result.size());
//		verify(doctorRepository).findAll(any(Pageable.class));
//		verify(doctorDto).convertDoctorIntoDto(doctorList);
//	}
//
//	@Test
//	public void getDoctorByIdTest() {
//		when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
//		Doctor found = doctorService.getDoctorById(1);
//		assertEquals("John", found.getFullName());
//	}
//
//	@Test
//	public void getDoctorByIdInvalidTest() {
//		when(doctorRepository.findById(999)).thenReturn(Optional.empty());
//
//		assertThrows(ResourceNotFoundException.class, () -> {
//			doctorService.getDoctorById(999);
//		});
//	}
//
//	public void updateDoctorTest() {
//		Doctor updated = new Doctor();
//		updated.setFullName("Jane");
//		updated.setExperienceYear(15);
//
//		when(doctorRepository.getDoctorByUsername("john")).thenReturn(doctor);
//		when(doctorRepository.save(doctor)).thenReturn(doctor);
//
//		Doctor result = doctorService.updateDoctor("john", updated);
//		assertEquals("Jane", result.getFullName());
//		assertEquals(15, result.getExperienceYear());
//	}
//
//	@Test
//	public void getBySpecializationTest() {
//		List<Doctor> doctors = List.of(doctor);
//
//		when(doctorRepository.getBySpecialization("Cardiology")).thenReturn(doctors);
//		when(doctorDto.convertDoctorIntoDto(doctors)).thenReturn(List.of(dto));
//
//		List<DoctorDto> result = doctorService.getBySpecialization("Cardiology");
//
//		assertEquals(1, result.size());
//		assertEquals("Cardiology", result.get(0).getSpecialization());
//	}
//
//	@Test
//	public void searchByNameTest() {
//		List<Doctor> doctors = List.of(doctor);
//
//		when(doctorRepository.searchByName("John")).thenReturn(doctors);
//		when(doctorDto.convertDoctorIntoDto(doctors)).thenReturn(List.of(dto));
//
//		List<DoctorDto> result = doctorService.searchByName("John");
//
//		assertEquals(1, result.size());
//		assertTrue(result.get(0).getFullName().contains("John"));
//	}
//
//	@AfterEach
//	public void afterTest() {
//		doctor = null;
//		user = null;
//		department = null;
//		receptionist = null;
//		System.out.println("Objects cleaned");
//	}
//
//}
