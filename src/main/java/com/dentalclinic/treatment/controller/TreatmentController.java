package com.dentalclinic.treatment.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.treatment.dto.TreatmentDto;
import com.dentalclinic.treatment.service.TreatmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/treatments")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<TreatmentDto> createTreatment(@Valid @RequestBody TreatmentDto treatmentDto) {
        return new ResponseEntity<>(treatmentService.createTreatment(treatmentDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<List<TreatmentDto>> getAllTreatments() {
        return ResponseEntity.ok(treatmentService.getAllTreatments());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<List<TreatmentDto>> getTreatmentsByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(treatmentService.getTreatmentsByClinicId(clinicId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<TreatmentDto> getTreatmentById(@PathVariable Long id) {
        return ResponseEntity.ok(treatmentService.getTreatmentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<TreatmentDto> updateTreatment(@PathVariable Long id, @Valid @RequestBody TreatmentDto treatmentDto) {
        return ResponseEntity.ok(treatmentService.updateTreatment(id, treatmentDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<MessageResponse> deleteTreatment(@PathVariable Long id) {
        treatmentService.deleteTreatment(id);
        return ResponseEntity.ok(new MessageResponse("Treatment deleted successfully"));
    }
}
