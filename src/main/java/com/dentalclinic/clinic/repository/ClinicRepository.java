package com.dentalclinic.clinic.repository;

import com.dentalclinic.clinic.entity.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    List<Clinic> findByActiveTrue();
    
    // You can add more derived queries if you like
    // e.g., List<Clinic> findByNameContainingIgnoreCase(String name);
}
