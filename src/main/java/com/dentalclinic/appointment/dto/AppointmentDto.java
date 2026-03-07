package com.dentalclinic.appointment.dto;

import com.dentalclinic.appointment.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private Long id;

    @NotNull(message = "Patient ID is mandatory")
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;

    @NotNull(message = "Dentist ID is mandatory")
    private Long dentistId;
    private String dentistFirstName;
    private String dentistLastName;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;

    @NotNull(message = "Start time is mandatory")
    private LocalDateTime startTime;

    @NotNull(message = "End time is mandatory")
    private LocalDateTime endTime;

    private AppointmentStatus status;
    private String notes;

    private Long treatmentId;
    private String treatmentName;
}
