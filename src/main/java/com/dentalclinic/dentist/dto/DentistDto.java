package com.dentalclinic.dentist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DentistDto {
    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;

    // Derived from User entity (ReadOnly for responses)
    private String firstName;
    private String lastName;
    private String email;

    @NotBlank(message = "Specialization is mandatory")
    private String specialization;

    private String licenseNumber;
    private String biography;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;
}
