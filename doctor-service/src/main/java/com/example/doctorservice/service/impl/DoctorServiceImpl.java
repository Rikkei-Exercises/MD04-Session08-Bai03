package com.example.doctorservice.service.impl;

import com.example.doctorservice.dto.request.DoctorRequestDTO;
import com.example.doctorservice.dto.response.DoctorResponseDTO;
import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import com.example.doctorservice.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));
        return mapToDTO(doctor);
    }

    @Override
    @Transactional
    public DoctorResponseDTO createDoctor(DoctorRequestDTO request) {
        Doctor doctor = Doctor.builder()
                .name(request.getName())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .email(request.getEmail())
                .status(request.getStatus())
                .build();

        Doctor saved = doctorRepository.save(doctor);
        return mapToDTO(saved);
    }

    private DoctorResponseDTO mapToDTO(Doctor doctor) {
        return DoctorResponseDTO.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .build();
    }
}
