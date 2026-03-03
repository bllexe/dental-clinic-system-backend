package com.dentalclinic.clinic.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ClinicDto {
    private Long id;

    @NotBlank(message = "Clinic name is mandatory")
    private String name;

    private String address;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;

    private String taxNumber;

    private boolean active;

    private LocalDate subscriptionEndDate;
}
