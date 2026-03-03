package com.dentalclinic.dentist.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.dentist.dto.DentistDto;
import com.dentalclinic.dentist.service.DentistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<DentistDto> createDentist(@Valid @RequestBody DentistDto dentistDto) {
        return new ResponseEntity<>(dentistService.createDentist(dentistDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<DentistDto>> getAllDentists() {
        return ResponseEntity.ok(dentistService.getAllDentists());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<DentistDto>> getDentistsByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(dentistService.getDentistsByClinicId(clinicId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST')")
    public ResponseEntity<DentistDto> getDentistById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.getDentistById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<DentistDto> updateDentist(@PathVariable Long id, @Valid @RequestBody DentistDto dentistDto) {
        return ResponseEntity.ok(dentistService.updateDentist(id, dentistDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<MessageResponse> deleteDentist(@PathVariable Long id) {
        dentistService.deleteDentist(id);
        return ResponseEntity.ok(new MessageResponse("Dentist deleted successfully"));
    }
}
