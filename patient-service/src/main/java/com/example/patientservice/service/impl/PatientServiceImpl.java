package com.example.patientservice.service.impl;

import com.example.patientservice.dto.request.PatientRequestDTO;
import com.example.patientservice.dto.response.PatientResponseDTO;
import com.example.patientservice.entity.Patient;
import com.example.patientservice.exception.ResourceNotFoundException;
import com.example.patientservice.repository.PatientRepository;
import com.example.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO request) {
        Patient patient = Patient.builder()
                .id(request.getId())
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .medicalHistory(request.getMedicalHistory())
                .build();

        Patient saved = patientRepository.save(patient);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        return mapToDTO(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private PatientResponseDTO mapToDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phoneNumber(patient.getPhoneNumber())
                .address(patient.getAddress())
                .medicalHistory(patient.getMedicalHistory())
                .build();
    }
}
