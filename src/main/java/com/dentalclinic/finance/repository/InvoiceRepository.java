package com.dentalclinic.finance.repository;

import com.dentalclinic.finance.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByClinicIdAndDeletedFalse(Long clinicId);
    List<Invoice> findByPatientIdAndDeletedFalse(Long patientId);
    List<Invoice> findByAppointmentIdAndDeletedFalse(Long appointmentId);
    List<Invoice> findByDeletedFalse();
    
    // Check for unique invoice numbers within the same clinic
    boolean existsByInvoiceNumberAndClinicId(String invoiceNumber, Long clinicId);
}
