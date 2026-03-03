package com.dentalclinic.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id;

    // Optional user ID if patient has login credentials
    private Long userId; 
    
    // For direct info, if user is not linked, maybe client provides these otherwise it gets populated from User.
    // However, keeping simple - read only from User if linked, or filled if not linked.
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @NotBlank(message = "Identity number is mandatory")
    private String identityNumber;

    private LocalDate birthDate;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;
}
