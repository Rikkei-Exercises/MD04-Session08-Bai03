package com.example.appointmentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDTO {
    private Long patientId;
    private Long doctorId;
    private String patientName;
    private String appointmentTime;
    private String notes;
}
