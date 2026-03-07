package com.dentalclinic.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceDto {
    private Long id;

    @NotNull(message = "Patient ID is mandatory")
    private Long patientId;
    
    // Read-only info
    private String patientFirstName;
    private String patientLastName;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;

    private Long appointmentId;

    @NotBlank(message = "Invoice number is mandatory")
    private String invoiceNumber;

    @NotNull(message = "Total amount is mandatory")
    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private LocalDate dueDate;

    private String status; // "UNPAID", "PARTIAL", "PAID", "CANCELLED"
}
