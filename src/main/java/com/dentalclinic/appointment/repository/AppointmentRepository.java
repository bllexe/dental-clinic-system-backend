package com.dentalclinic.appointment.repository;

import com.dentalclinic.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByClinicIdAndDeletedFalse(Long clinicId);
    List<Appointment> findByDentistIdAndDeletedFalse(Long dentistId);
    List<Appointment> findByPatientIdAndDeletedFalse(Long patientId);
    
    // For checking conflicts
    List<Appointment> findByDentistIdAndStartTimeLessThanAndEndTimeGreaterThanAndDeletedFalse(
            Long dentistId, LocalDateTime endTime, LocalDateTime startTime);
            
    List<Appointment> findByDeletedFalse();
}
