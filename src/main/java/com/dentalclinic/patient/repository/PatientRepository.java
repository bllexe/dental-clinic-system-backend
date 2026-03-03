package com.dentalclinic.patient.repository;

import com.dentalclinic.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByClinicIdAndDeletedFalse(Long clinicId);
    List<Patient> findByDeletedFalse();
}
