package com.example.doctorservice.config;

import com.example.doctorservice.entity.Doctor;
import com.example.doctorservice.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    @Override
    public void run(String... args) {
        if (doctorRepository.count() == 0) {
            List<Doctor> initialDoctors = List.of(
                    Doctor.builder()
                            .name("Nguyen Van Binh")
                            .specialization("Noi khoa")
                            .experienceYears(10)
                            .email("binh.nguyen@hospital.vn")
                            .status(true)
                            .build(),
                    Doctor.builder()
                            .name("Tran Thi Mai")
                            .specialization("Nhi khoa")
                            .experienceYears(8)
                            .email("mai.tran@hospital.vn")
                            .status(true)
                            .build(),
                    Doctor.builder()
                            .name("Le Hoang Nam")
                            .specialization("Ngoai khoa")
                            .experienceYears(15)
                            .email("nam.le@hospital.vn")
                            .status(true)
                            .build()
            );
            doctorRepository.saveAll(initialDoctors);
            log.info("Initialized {} sample doctors into doctor_db", initialDoctors.size());
        }
    }
}
