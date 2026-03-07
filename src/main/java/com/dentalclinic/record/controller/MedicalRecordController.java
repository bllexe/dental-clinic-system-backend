package com.dentalclinic.record.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.record.dto.MedicalRecordDto;
import com.dentalclinic.record.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<MedicalRecordDto> createRecord(@Valid @RequestBody MedicalRecordDto dto) {
        return new ResponseEntity<>(medicalRecordService.createRecord(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllRecords());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST')")
    public ResponseEntity<List<MedicalRecordDto>> getRecordsByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByClinicId(clinicId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<List<MedicalRecordDto>> getRecordsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(medicalRecordService.getRecordsByPatientId(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<List<MedicalRecordDto>> getRecordsByAppointmentId(@PathVariable Long appointmentId) {
         return ResponseEntity.ok(medicalRecordService.getRecordsByAppointmentId(appointmentId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<MedicalRecordDto> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('DENTIST')")
    public ResponseEntity<MedicalRecordDto> updateRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecordDto dto) {
        return ResponseEntity.ok(medicalRecordService.updateRecord(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<MessageResponse> deleteRecord(@PathVariable Long id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.ok(new MessageResponse("Medical record deleted successfully"));
    }
}
