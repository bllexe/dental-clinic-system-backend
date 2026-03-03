package com.dentalclinic.dentist.repository;

import com.dentalclinic.dentist.entity.Dentist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Long> {
    List<Dentist> findByClinicIdAndDeletedFalse(Long clinicId);
    List<Dentist> findByDeletedFalse();
}
