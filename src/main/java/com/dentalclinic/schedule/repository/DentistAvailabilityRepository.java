package com.dentalclinic.schedule.repository;

import com.dentalclinic.schedule.entity.DentistAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface DentistAvailabilityRepository extends JpaRepository<DentistAvailability, Long> {
    List<DentistAvailability> findByClinicIdAndDeletedFalse(Long clinicId);
    List<DentistAvailability> findByDentistIdAndDeletedFalse(Long dentistId);
    Optional<DentistAvailability> findByDentistIdAndDayOfWeekAndDeletedFalse(Long dentistId, DayOfWeek dayOfWeek);
    List<DentistAvailability> findByDeletedFalse();
}
