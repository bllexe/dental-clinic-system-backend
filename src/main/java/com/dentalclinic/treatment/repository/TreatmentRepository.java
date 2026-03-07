package com.dentalclinic.treatment.repository;

import com.dentalclinic.treatment.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
    List<Treatment> findByClinicIdAndDeletedFalse(Long clinicId);
    List<Treatment> findByDeletedFalse();
}
