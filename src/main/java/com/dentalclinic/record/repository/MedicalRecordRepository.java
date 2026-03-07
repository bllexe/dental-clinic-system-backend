package com.dentalclinic.record.repository;

import com.dentalclinic.record.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByClinicIdAndDeletedFalse(Long clinicId);
    List<MedicalRecord> findByPatientIdAndDeletedFalse(Long patientId);
    List<MedicalRecord> findByAppointmentIdAndDeletedFalse(Long appointmentId);
    List<MedicalRecord> findByDeletedFalse();
}
