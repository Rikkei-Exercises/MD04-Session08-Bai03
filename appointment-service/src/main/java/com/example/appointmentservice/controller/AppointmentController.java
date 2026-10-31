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
     * Endpoint kiem tra danh sach bac si (co Fallback getDoctorFallback tra ve status 503 khi Doctor-Service loi)
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
     * Endpoint kiem tra chi tiet 1 bac si
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
     * Endpoint dat lich kham
     */
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.AddAppointment(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET dat lich nhanh
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
