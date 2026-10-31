package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.request.AppointmentRequestDTO;
import com.example.appointmentservice.dto.response.AppointmentResponseDTO;
import com.example.appointmentservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> addAppointment(@RequestBody AppointmentRequestDTO request) {
        AppointmentResponseDTO response = appointmentService.AddAppointment(request);
        return ResponseEntity.ok(response);
    }

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
