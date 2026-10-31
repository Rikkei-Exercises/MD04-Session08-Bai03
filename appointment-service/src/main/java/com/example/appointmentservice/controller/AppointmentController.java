package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.request.AppointmentRequestDTO;
import com.example.appointmentservice.dto.response.ApiResponseError;
import com.example.appointmentservice.dto.response.AppointmentResponseDTO;
import com.example.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Endpoint gọi sang Patient-Service (Áp dụng Retry 3 lần, mỗi lần cách 2s)
     */
    @GetMapping("/patients/{patientId}")
    public ResponseEntity<?> getPatient(@PathVariable Long patientId) {
        Object result = appointmentService.getPatient(patientId);
        if (result instanceof ApiResponseError error) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint kiểm tra danh sách bác sĩ (Có Circuit Breaker)
     */
    @GetMapping("/doctors")
    public ResponseEntity<?> getDoctors() {
        Object result = appointmentService.getDoctor();
        if (result instanceof ApiResponseError error) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint kiểm tra chi tiết 1 bác sĩ
     */
    @GetMapping("/doctors/{doctorId}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long doctorId) {
        Object result = appointmentService.getDoctor(doctorId);
        if (result instanceof ApiResponseError error) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint đặt lịch khám
     */
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.AddAppointment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET đặt lịch nhanh
     */
    @GetMapping("/book/{doctorId}")
    public ResponseEntity<AppointmentResponseDTO> quickBook(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "Nguyen Van An") String patientName) {
        AppointmentRequestDTO request = AppointmentRequestDTO.builder()
                .doctorId(doctorId)
                .patientName(patientName)
                .appointmentTime("2026-11-01 09:30")
                .notes("Kham tong quat")
                .build();
        return ResponseEntity.ok(appointmentService.AddAppointment(request));
    }
}
