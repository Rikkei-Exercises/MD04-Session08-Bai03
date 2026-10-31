package com.example.appointmentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDTO {
    private String status;
    private String message;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private String appointmentTime;
}
