package com.example.doctorservice;

import com.example.doctorservice.dto.response.DoctorResponseDTO;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import com.example.doctorservice.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    void testGetAllDoctors() {
        Doctor doc1 = Doctor.builder()
                .id(1L)
                .name("Nguyen Van Binh")
                .specialization("Noi khoa")
                .experienceYears(10)
                .email("binh.nguyen@hospital.vn")
                .status(true)
                .build();

        Doctor doc2 = Doctor.builder()
                .id(2L)
                .name("Tran Thi Mai")
                .specialization("Nhi khoa")
                .experienceYears(8)
                .email("mai.tran@hospital.vn")
                .status(true)
                .build();

        when(doctorRepository.findAll()).thenReturn(List.of(doc1, doc2));

        List<DoctorResponseDTO> result = doctorService.getAllDoctors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Nguyen Van Binh", result.get(0).getName());
        assertEquals("Noi khoa", result.get(0).getSpecialization());
    }
}
