package com.example.patientservice.service;

import com.example.patientservice.dto.request.PatientRequestDTO;
import com.example.patientservice.dto.response.PatientResponseDTO;

import java.util.List;

public interface PatientService {

    PatientResponseDTO createPatient(PatientRequestDTO request);

    PatientResponseDTO getPatientById(Long id);

    List<PatientResponseDTO> getAllPatients();
}
