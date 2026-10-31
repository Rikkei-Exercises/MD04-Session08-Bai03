package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.request.AppointmentRequestDTO;
import com.example.appointmentservice.dto.response.AppointmentResponseDTO;
import com.example.appointmentservice.dto.response.DoctorResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "AddAppointmentFallback")
    public AppointmentResponseDTO AddAppointment(AppointmentRequestDTO request) {
        log.info("Dang kiem tra lich truc bac si tu Doctor-Service (doctorId={})...", request.getDoctorId());

        String doctorUrl = "http://doctor-service/api/v1/doctors/" + request.getDoctorId();
        DoctorResponseDTO doctor = restTemplate.getForObject(doctorUrl, DoctorResponseDTO.class);

        log.info("Kiem tra thanh cong, bac si hop le: {}", doctor != null ? doctor.getName() : "Khong xac dinh");

        return AppointmentResponseDTO.builder()
                .status("SUCCESS")
                .message("Dat lich kham thanh cong!")
                .doctorId(request.getDoctorId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown")
                .patientName(request.getPatientName())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }

    public AppointmentResponseDTO AddAppointmentFallback(AppointmentRequestDTO request, Throwable throwable) {
        log.warn("Fallback duoc kich hoat cho AddAppointment vi Doctor-Service gap su co: {}", throwable.toString());

        return AppointmentResponseDTO.builder()
                .status("CIRCUIT_BREAKER_FALLBACK")
                .message("Doctor-Service hien dang gap su co hoac mach da NGAT (OPEN). He thong tra ve loi ngay lap tuc de bao ve he thong.")
                .doctorId(request.getDoctorId())
                .doctorName("Khong the lay thong tin bac si")
                .patientName(request.getPatientName())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }
}
