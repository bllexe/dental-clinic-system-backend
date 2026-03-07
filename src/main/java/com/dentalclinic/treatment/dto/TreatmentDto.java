package com.dentalclinic.treatment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TreatmentDto {
    private Long id;

    @NotBlank(message = "Treatment name is mandatory")
    private String name;

    private String description;
    
    @NotNull(message = "Default price is mandatory")
    private BigDecimal defaultPrice;
    
    @NotNull(message = "Estimated duration is mandatory")
    private Integer estimatedDurationMinutes;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;
}
