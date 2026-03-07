package com.dentalclinic.schedule.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.schedule.dto.DentistAvailabilityDto;
import com.dentalclinic.schedule.service.DentistAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/availabilities")
@RequiredArgsConstructor
public class DentistAvailabilityController {

    private final DentistAvailabilityService availabilityService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<DentistAvailabilityDto> createAvailability(@Valid @RequestBody DentistAvailabilityDto dto) {
        return new ResponseEntity<>(availabilityService.createAvailability(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<DentistAvailabilityDto>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.getAllAvailabilities());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<List<DentistAvailabilityDto>> getAvailabilitiesByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesByClinicId(clinicId));
    }

    @GetMapping("/dentist/{dentistId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<List<DentistAvailabilityDto>> getAvailabilitiesByDentistId(@PathVariable Long dentistId) {
        return ResponseEntity.ok(availabilityService.getAvailabilitiesByDentistId(dentistId));
    }

    @GetMapping("/dentist/{dentistId}/{dayOfWeek}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<DentistAvailabilityDto> getAvailabilityByDentistIdAndDay(
            @PathVariable Long dentistId, @PathVariable DayOfWeek dayOfWeek) {
        return ResponseEntity.ok(availabilityService.getAvailabilityByDentistIdAndDay(dentistId, dayOfWeek));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<DentistAvailabilityDto> getAvailabilityById(@PathVariable Long id) {
        return ResponseEntity.ok(availabilityService.getAvailabilityById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<DentistAvailabilityDto> updateAvailability(@PathVariable Long id, @Valid @RequestBody DentistAvailabilityDto dto) {
        return ResponseEntity.ok(availabilityService.updateAvailability(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<MessageResponse> deleteAvailability(@PathVariable Long id) {
        availabilityService.deleteAvailability(id);
        return ResponseEntity.ok(new MessageResponse("Availability deleted successfully"));
    }
}
