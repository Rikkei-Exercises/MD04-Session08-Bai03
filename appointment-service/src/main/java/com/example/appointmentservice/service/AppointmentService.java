package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.request.AppointmentRequestDTO;
import com.example.appointmentservice.dto.response.ApiResponseError;
import com.example.appointmentservice.dto.response.AppointmentResponseDTO;
import com.example.appointmentservice.dto.response.DoctorResponseDTO;
import com.example.appointmentservice.dto.response.PatientResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {

    private final RestTemplate restTemplate;

    // =========================================================================
    // 1. GỌI PATIENT-SERVICE VỚI @Retry(name = "patientRetry")
    // =========================================================================

    /**
     * Gọi sang Patient-Service để lấy thông tin bệnh nhân.
     * Áp dụng @Retry: tự động thử lại tối đa 3 lần, mỗi lần cách nhau 2s nếu gặp lỗi mạng.
     */
    @Retry(name = "patientRetry", fallbackMethod = "getPatientFallback")
    public Object getPatient(Long patientId) {
        log.info("Đang gọi sang Patient-Service lấy thông tin bệnh nhân id={}...", patientId);
        String patientUrl = "http://patient-service/api/v1/patients/" + patientId;
        return restTemplate.getForObject(patientUrl, Object.class);
    }

    /**
     * Fallback được gọi khi đã thử lại 3 lần mà vẫn thất bại
     */
    public ApiResponseError getPatientFallback(Long patientId, Exception e) {
        log.warn("getPatientFallback được kích hoạt sau 3 lần Retry thất bại cho patientId={}: {}", patientId, e.toString());
        return ApiResponseError.of(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Hệ thống Patient-Service hiện không phản hồi sau 3 lần thử lại. Vui lòng thử lại sau!"
        );
    }

    // =========================================================================
    // 2. GỌI DOCTOR-SERVICE VỚI @CircuitBreaker(name = "doctorServiceCB")
    // =========================================================================

    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "getDoctorFallback")
    public Object getDoctor() {
        log.info("Gọi sang Doctor-Service để lấy danh sách bác sĩ...");
        String doctorUrl = "http://doctor-service/api/v1/doctors";
        return restTemplate.getForObject(doctorUrl, Object.class);
    }

    public ApiResponseError getDoctorFallback(Exception e) {
        log.warn("getDoctorFallback được kích hoạt do lỗi: {}", e.toString());
        return ApiResponseError.of(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây"
        );
    }

    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "getDoctorByIdFallback")
    public Object getDoctor(Long doctorId) {
        log.info("Gọi sang Doctor-Service để lấy thông tin bác sĩ id={}...", doctorId);
        String doctorUrl = "http://doctor-service/api/v1/doctors/" + doctorId;
        return restTemplate.getForObject(doctorUrl, Object.class);
    }

    public ApiResponseError getDoctorByIdFallback(Long doctorId, Exception e) {
        log.warn("getDoctorByIdFallback được kích hoạt cho doctorId={}: {}", doctorId, e.toString());
        return getDoctorFallback(e);
    }

    // =========================================================================
    // 3. ĐẶT LỊCH HẸN
    // =========================================================================

    @CircuitBreaker(name = "doctorServiceCB", fallbackMethod = "AddAppointmentFallback")
    public AppointmentResponseDTO AddAppointment(AppointmentRequestDTO request) {
        log.info("Đang kiểm tra lịch trực bác sĩ từ Doctor-Service (doctorId={})...", request.getDoctorId());

        String doctorUrl = "http://doctor-service/api/v1/doctors/" + request.getDoctorId();
        DoctorResponseDTO doctor = restTemplate.getForObject(doctorUrl, DoctorResponseDTO.class);

        log.info("Kiểm tra thành công, bác sĩ hợp lệ: {}", doctor != null ? doctor.getName() : "Không xác định");

        return AppointmentResponseDTO.builder()
                .status("SUCCESS")
                .message("Đặt lịch khám thành công!")
                .doctorId(request.getDoctorId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown")
                .patientName(request.getPatientName())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }

    public AppointmentResponseDTO AddAppointmentFallback(AppointmentRequestDTO request, Throwable throwable) {
        log.warn("Fallback được kích hoạt cho AddAppointment vì Doctor-Service gặp sự cố: {}", throwable.toString());

        return AppointmentResponseDTO.builder()
                .status("CIRCUIT_BREAKER_FALLBACK")
                .message("Doctor-Service hiện đang gặp sự cố hoặc mạch đã NGẮT (OPEN). Hệ thống trả về lỗi ngay lập tức để bảo vệ hệ thống.")
                .doctorId(request.getDoctorId())
                .doctorName("Không thể lấy thông tin bác sĩ")
                .patientName(request.getPatientName())
                .appointmentTime(request.getAppointmentTime())
                .build();
    }
}
