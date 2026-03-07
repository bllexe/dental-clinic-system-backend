package com.dentalclinic.finance.service;

import com.dentalclinic.finance.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceDto dto);
    InvoiceDto getInvoiceById(Long id);
    List<InvoiceDto> getAllInvoices();
    List<InvoiceDto> getInvoicesByClinicId(Long clinicId);
    List<InvoiceDto> getInvoicesByPatientId(Long patientId);
    List<InvoiceDto> getInvoicesByAppointmentId(Long appointmentId);
    InvoiceDto updateInvoice(Long id, InvoiceDto dto);
    void deleteInvoice(Long id);
}
