package com.dentalclinic.finance.service;

import com.dentalclinic.appointment.entity.Appointment;
import com.dentalclinic.appointment.repository.AppointmentRepository;
import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.finance.dto.InvoiceDto;
import com.dentalclinic.finance.entity.Invoice;
import com.dentalclinic.finance.repository.InvoiceRepository;
import com.dentalclinic.patient.entity.Patient;
import com.dentalclinic.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public InvoiceDto createInvoice(InvoiceDto dto) {
        if (invoiceRepository.existsByInvoiceNumberAndClinicId(dto.getInvoiceNumber(), dto.getClinicId())) {
            throw new IllegalArgumentException("Invoice number already exists for this clinic.");
        }

        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Invoice invoice = new Invoice();
        invoice.setClinic(clinic);
        invoice.setPatient(patient);
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setPaidAmount(dto.getPaidAmount());
        invoice.setDueDate(dto.getDueDate());
        invoice.setStatus(dto.getStatus() != null ? dto.getStatus() : "UNPAID");

        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            invoice.setAppointment(appointment);
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToDto(savedInvoice);
    }

    @Override
    public InvoiceDto getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        
        if (invoice.isDeleted()) {
             throw new ResourceNotFoundException("Invoice not found");
        }
        return mapToDto(invoice);
    }

    @Override
    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getInvoicesByClinicId(Long clinicId) {
        return invoiceRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getInvoicesByPatientId(Long patientId) {
        return invoiceRepository.findByPatientIdAndDeletedFalse(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> getInvoicesByAppointmentId(Long appointmentId) {
        return invoiceRepository.findByAppointmentIdAndDeletedFalse(appointmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto updateInvoice(Long id, InvoiceDto dto) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (invoice.isDeleted()) {
           throw new ResourceNotFoundException("Invoice not found.");
        }
        
        // Prevent changing invoice number to one that already exists
        if (!invoice.getInvoiceNumber().equals(dto.getInvoiceNumber()) &&
            invoiceRepository.existsByInvoiceNumberAndClinicId(dto.getInvoiceNumber(), dto.getClinicId())) {
            throw new IllegalArgumentException("Invoice number already exists for this clinic.");
        }

        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setTotalAmount(dto.getTotalAmount());
        invoice.setPaidAmount(dto.getPaidAmount());
        invoice.setDueDate(dto.getDueDate());
        invoice.setStatus(dto.getStatus());

        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            invoice.setAppointment(appointment);
        } else {
             invoice.setAppointment(null);
        }

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToDto(updatedInvoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        invoice.setDeleted(true);
        invoice.setStatus("CANCELLED");
        invoiceRepository.save(invoice);
    }

    private InvoiceDto mapToDto(Invoice invoice) {
        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setDueDate(invoice.getDueDate());
        dto.setStatus(invoice.getStatus());

        if (invoice.getClinic() != null) {
            dto.setClinicId(invoice.getClinic().getId());
        }

        if (invoice.getPatient() != null) {
            dto.setPatientId(invoice.getPatient().getId());
            if (invoice.getPatient().getUser() != null) {
                 dto.setPatientFirstName(invoice.getPatient().getUser().getFirstName());
                 dto.setPatientLastName(invoice.getPatient().getUser().getLastName());
            } else {
                 dto.setPatientFirstName(invoice.getPatient().getEmergencyContactName());
            }
        }

        if (invoice.getAppointment() != null) {
            dto.setAppointmentId(invoice.getAppointment().getId());
        }

        return dto;
    }
}
