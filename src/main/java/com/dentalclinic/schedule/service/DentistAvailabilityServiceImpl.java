package com.dentalclinic.schedule.service;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.dentist.entity.Dentist;
import com.dentalclinic.dentist.repository.DentistRepository;
import com.dentalclinic.schedule.dto.DentistAvailabilityDto;
import com.dentalclinic.schedule.entity.DentistAvailability;
import com.dentalclinic.schedule.repository.DentistAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistAvailabilityServiceImpl implements DentistAvailabilityService {

    private final DentistAvailabilityRepository availabilityRepository;
    private final DentistRepository dentistRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public DentistAvailabilityDto createAvailability(DentistAvailabilityDto availabilityDto) {
        // Validation to prevent duplicate schedules for the same day
        Optional<DentistAvailability> existing = availabilityRepository
                .findByDentistIdAndDayOfWeekAndDeletedFalse(availabilityDto.getDentistId(), availabilityDto.getDayOfWeek());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Availability for this day already exists. Please update it instead.");
        }

        Dentist dentist = dentistRepository.findById(availabilityDto.getDentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with id: " + availabilityDto.getDentistId()));

        Clinic clinic = clinicRepository.findById(availabilityDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + availabilityDto.getClinicId()));

        DentistAvailability availability = new DentistAvailability();
        availability.setDentist(dentist);
        availability.setClinic(clinic);
        availability.setDayOfWeek(availabilityDto.getDayOfWeek());
        availability.setStartTime(availabilityDto.getStartTime());
        availability.setEndTime(availabilityDto.getEndTime());
        availability.setDayOff(availabilityDto.isDayOff());

        DentistAvailability savedAvailability = availabilityRepository.save(availability);
        return mapToDto(savedAvailability);
    }

    @Override
    public DentistAvailabilityDto getAvailabilityById(Long id) {
        DentistAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + id));

        if (availability.isDeleted()) {
             throw new ResourceNotFoundException("Availability not found with id: " + id);
        }

        return mapToDto(availability);
    }

    @Override
    public List<DentistAvailabilityDto> getAllAvailabilities() {
        return availabilityRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DentistAvailabilityDto> getAvailabilitiesByClinicId(Long clinicId) {
        return availabilityRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DentistAvailabilityDto> getAvailabilitiesByDentistId(Long dentistId) {
        return availabilityRepository.findByDentistIdAndDeletedFalse(dentistId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DentistAvailabilityDto getAvailabilityByDentistIdAndDay(Long dentistId, DayOfWeek dayOfWeek) {
        DentistAvailability availability = availabilityRepository
                .findByDentistIdAndDayOfWeekAndDeletedFalse(dentistId, dayOfWeek)
                .orElseThrow(() -> new ResourceNotFoundException("No schedule found for this dentist on given day."));
        return mapToDto(availability);
    }

    @Override
    public DentistAvailabilityDto updateAvailability(Long id, DentistAvailabilityDto availabilityDto) {
        DentistAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + id));

        if (availability.isDeleted()) {
             throw new ResourceNotFoundException("Availability not found with id: " + id);
        }

        // Change values
        availability.setDayOfWeek(availabilityDto.getDayOfWeek());
        availability.setStartTime(availabilityDto.getStartTime());
        availability.setEndTime(availabilityDto.getEndTime());
        availability.setDayOff(availabilityDto.isDayOff());

        DentistAvailability updatedAvailability = availabilityRepository.save(availability);
        return mapToDto(updatedAvailability);
    }

    @Override
    public void deleteAvailability(Long id) {
        DentistAvailability availability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found with id: " + id));

        availability.setDeleted(true);
        availabilityRepository.save(availability);
    }

    private DentistAvailabilityDto mapToDto(DentistAvailability availability) {
        DentistAvailabilityDto dto = new DentistAvailabilityDto();
        dto.setId(availability.getId());
        dto.setDayOfWeek(availability.getDayOfWeek());
        dto.setStartTime(availability.getStartTime());
        dto.setEndTime(availability.getEndTime());
        dto.setDayOff(availability.isDayOff());
        
        if (availability.getDentist() != null) {
            dto.setDentistId(availability.getDentist().getId());
            if (availability.getDentist().getUser() != null) {
                dto.setDentistFirstName(availability.getDentist().getUser().getFirstName());
                dto.setDentistLastName(availability.getDentist().getUser().getLastName());
            }
        }

        if (availability.getClinic() != null) {
            dto.setClinicId(availability.getClinic().getId());
        }

        return dto;
    }
}
