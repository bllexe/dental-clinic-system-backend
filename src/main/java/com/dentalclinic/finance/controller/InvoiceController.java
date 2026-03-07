package com.dentalclinic.finance.controller;

import com.dentalclinic.auth.dto.MessageResponse;
import com.dentalclinic.finance.dto.InvoiceDto;
import com.dentalclinic.finance.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<InvoiceDto> createInvoice(@Valid @RequestBody InvoiceDto dto) {
        return new ResponseEntity<>(invoiceService.createInvoice(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
        return ResponseEntity.ok(invoiceService.getAllInvoices());
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByClinicId(@PathVariable Long clinicId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByClinicId(clinicId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('PATIENT')")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(invoiceService.getInvoicesByPatientId(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<List<InvoiceDto>> getInvoicesByAppointmentId(@PathVariable Long appointmentId) {
         return ResponseEntity.ok(invoiceService.getInvoicesByAppointmentId(appointmentId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF') or hasRole('PATIENT')")
    public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN') or hasRole('STAFF')")
    public ResponseEntity<InvoiceDto> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceDto dto) {
        return ResponseEntity.ok(invoiceService.updateInvoice(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('CLINIC_ADMIN')")
    public ResponseEntity<MessageResponse> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.ok(new MessageResponse("Invoice deleted/cancelled successfully"));
    }
}
