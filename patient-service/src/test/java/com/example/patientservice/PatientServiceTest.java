package com.example.patientservice;

import com.example.patientservice.dto.request.PatientRequestDTO;
import com.example.patientservice.dto.response.PatientResponseDTO;
import com.example.patientservice.entity.Patient;
import com.example.patientservice.repository.PatientRepository;
import com.example.patientservice.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    @Test
    void testCreatePatient() {
        PatientRequestDTO request = PatientRequestDTO.builder()
                .fullName("Nguyen Van A")
                .dateOfBirth(LocalDate.of(1995, 5, 20))
                .gender("Nam")
                .phoneNumber("0912345678")
                .address("Ha Noi")
                .medicalHistory("Di ung thuoc penicillin")
                .build();

        Patient saved = Patient.builder()
                .id(1L)
                .fullName("Nguyen Van A")
                .dateOfBirth(LocalDate.of(1995, 5, 20))
                .gender("Nam")
                .phoneNumber("0912345678")
                .address("Ha Noi")
                .medicalHistory("Di ung thuoc penicillin")
                .build();

        when(patientRepository.save(any(Patient.class))).thenReturn(saved);

        PatientResponseDTO response = patientService.createPatient(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Nguyen Van A", response.getFullName());
        assertEquals("Ha Noi", response.getAddress());
        assertEquals("Di ung thuoc penicillin", response.getMedicalHistory());
        verify(patientRepository).save(any(Patient.class));
    }
}
