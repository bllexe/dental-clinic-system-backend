package com.dentalclinic.appointment.controller;

import com.dentalclinic.appointment.dto.AppointmentDto;
import com.dentalclinic.appointment.enums.AppointmentStatus;
import com.dentalclinic.appointment.service.AppointmentService;
import com.dentalclinic.auth.dto.MessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentDto dto) {
        return new ResponseEntity<>(appointmentService.createAppointment(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByClinicId(clinicId));
    }

    @GetMapping("/dentist/{dentistId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDentistId(@PathVariable Long dentistId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDentistId(dentistId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST') or hasRole('PATIENT')")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDto dto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, dto));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('DENTIST')")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        AppointmentStatus status = AppointmentStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('PATIENT')")
    public ResponseEntity<MessageResponse> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok(new MessageResponse("Appointment cancelled/deleted successfully"));
    }
}
