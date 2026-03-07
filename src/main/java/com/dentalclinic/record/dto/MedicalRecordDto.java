package com.dentalclinic.record.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordDto {
    private Long id;

    @NotNull(message = "Patient ID is mandatory")
    private Long patientId;
    
    // For read-only mapping
    private String patientFirstName;
    private String patientLastName;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;

    // Optional because a medical record could be created outside of an appointment
    private Long appointmentId;

    private String diagnosis;
    private String treatmentNotes;
    private String prescription;
}
