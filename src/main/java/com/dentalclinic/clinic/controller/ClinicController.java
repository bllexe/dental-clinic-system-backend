package com.dentalclinic.clinic.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.clinic.dto.ClinicDto;
import com.dentalclinic.clinic.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')") // Usually only a system admin creates new clinics
    public ResponseEntity<ClinicDto> createClinic(@Valid @RequestBody ClinicDto clinicDto) {
        return new ResponseEntity<>(clinicService.createClinic(clinicDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<ClinicDto>> getAllClinics() {
        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<List<ClinicDto>> getActiveClinics() {
        return ResponseEntity.ok(clinicService.getActiveClinics());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ClinicDto> getClinicById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(clinicService.getClinicById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<ClinicDto> updateClinic(@PathVariable(name = "id") Long id,
                                                  @Valid @RequestBody ClinicDto clinicDto) {
        return ResponseEntity.ok(clinicService.updateClinic(id, clinicDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<MessageResponse> deleteClinic(@PathVariable(name = "id") Long id) {
        clinicService.deleteClinic(id);
        return ResponseEntity.ok(new MessageResponse("Clinic deleted successfully."));
    }
}
