package com.dentalclinic.schedule.service;

import com.dentalclinic.schedule.dto.DentistAvailabilityDto;

import java.time.DayOfWeek;
import java.util.List;

public interface DentistAvailabilityService {
    DentistAvailabilityDto createAvailability(DentistAvailabilityDto availabilityDto);
    DentistAvailabilityDto getAvailabilityById(Long id);
    List<DentistAvailabilityDto> getAllAvailabilities();
    List<DentistAvailabilityDto> getAvailabilitiesByClinicId(Long clinicId);
    List<DentistAvailabilityDto> getAvailabilitiesByDentistId(Long dentistId);
    DentistAvailabilityDto getAvailabilityByDentistIdAndDay(Long dentistId, DayOfWeek dayOfWeek);
    DentistAvailabilityDto updateAvailability(Long id, DentistAvailabilityDto availabilityDto);
    void deleteAvailability(Long id);
}
