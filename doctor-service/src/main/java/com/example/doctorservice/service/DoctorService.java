package com.example.doctorservice.service;

import com.example.doctorservice.dto.request.DoctorRequestDTO;
import com.example.doctorservice.dto.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {

    List<DoctorResponseDTO> getAllDoctors();

    DoctorResponseDTO getDoctorById(Long id);

    DoctorResponseDTO createDoctor(DoctorRequestDTO request);
}

