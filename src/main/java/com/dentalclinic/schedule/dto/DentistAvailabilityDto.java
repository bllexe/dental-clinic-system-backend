package com.dentalclinic.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;


@Data
public class DentistAvailabilityDto {
    private Long id;

    @NotNull(message = "Dentist ID is mandatory")
    private Long dentistId;
    
    // For read operations
    private String dentistFirstName;
    private String dentistLastName;

    @NotNull(message = "Clinic ID is mandatory")
    private Long clinicId;

    @NotNull(message = "Day of week is mandatory")
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private boolean isDayOff;
}
