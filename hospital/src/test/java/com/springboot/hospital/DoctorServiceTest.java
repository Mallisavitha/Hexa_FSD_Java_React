package com.springboot.hospital;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.springboot.hospital.dto.DoctorDto;
import com.springboot.hospital.model.*;
import com.springboot.hospital.repository.*;
import com.springboot.hospital.service.DoctorService;
import com.springboot.hospital.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private ReceptionistRepository receptionistRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DoctorSlotRepository doctorSlotRepository;
    @Mock
    private UserService userService;
    @Mock
    private DoctorDto doctorDto;

    private Doctor doctor;
    private User user;
    private Department department;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("drjohn");

        department = new Department();
        department.setDepartmentId(1);
        department.setName("Cardiology");

        doctor = new Doctor();
        doctor.setDoctorId(1);
        doctor.setFullName("Dr. John");
        doctor.setUser(user);
        doctor.setDepartment(department);
        doctor.setSpecialization("Cardiology");
    }

    @Test
    void insertDoctor_Valid() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        when(userService.signUp(user)).thenReturn(user);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor saved = doctorService.insertDoctor(1, doctor, "admin");

        assertNotNull(saved);
        assertEquals("Dr. John", saved.getFullName());
        assertEquals("admin", saved.getAddedBy());
    }

    @Test
    void getDoctorById_Valid() {
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Doctor fetched = doctorService.getDoctorById(1);
        assertEquals("Dr. John", fetched.getFullName());
    }

    @Test
    void getAllDoctors_Valid() {
        List<Doctor> list = List.of(doctor);
        List<DoctorDto> dtoList = List.of(new DoctorDto());

        Page<Doctor> page = new PageImpl<>(list);
        when(doctorRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(doctorDto.convertDoctorIntoDto(list)).thenReturn(dtoList);

        List<?> result = doctorService.getAllDoctors(0, 10);
        assertEquals(1, result.size());
    }

    @Test
    void updateDoctor_Valid() {
        when(doctorRepository.getDoctorByUsername("drjohn")).thenReturn(doctor);

        Doctor updated = new Doctor();
        updated.setFullName("Dr. John M.D.");
        updated.setSpecialization("Cardiology");
        updated.setExperienceYear(10);
        updated.setQualification("MD");
        updated.setDesignation("Senior Consultant");
        updated.setContact("1234567890");

        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor result = doctorService.updateDoctor("drjohn", updated);
        assertEquals("Dr. John M.D.", result.getFullName());
        assertEquals(10, result.getExperienceYear());
    }

    @Test
    void getDoctorByUsername_Test() {
        when(doctorRepository.getDoctorByUsername("drjohn")).thenReturn(doctor);
        Doctor result = doctorService.getDoctorByUsername("drjohn");
        assertEquals("Dr. John", result.getFullName());
    }

    @Test
    void getBySpecialization_Test() {
        List<Doctor> list = List.of(doctor);
        List<DoctorDto> dtoList = List.of(new DoctorDto());

        when(doctorRepository.getBySpecialization("Cardiology")).thenReturn(list);
        when(doctorDto.convertDoctorIntoDto(list)).thenReturn(dtoList);

        List<DoctorDto> result = doctorService.getBySpecialization("Cardiology");
        assertEquals(1, result.size());
    }

    @Test
    void getDoctorByName_Test() {
        when(doctorRepository.getDoctorByName("John")).thenReturn(List.of(doctor));
        List<Doctor> result = doctorService.getDoctorByName("John");
        assertEquals(1, result.size());
    }

    @Test
    void getAllSpecializations_Test() {
        List<String> specs = List.of("Cardiology", "Neurology");
        when(doctorRepository.findAllDistinctSpecializations()).thenReturn(specs);
        List<String> result = doctorService.getAllSpecializations();
        assertEquals(2, result.size());
    }

    @AfterEach
    void tearDown() {
        doctor = null;
        user = null;
        department = null;
    }
}
