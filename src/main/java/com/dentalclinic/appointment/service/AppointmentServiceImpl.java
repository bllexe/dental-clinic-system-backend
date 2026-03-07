package com.dentalclinic.appointment.service;

import com.dentalclinic.appointment.dto.AppointmentDto;
import com.dentalclinic.appointment.entity.Appointment;
import com.dentalclinic.appointment.enums.AppointmentStatus;
import com.dentalclinic.appointment.repository.AppointmentRepository;
import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.dentist.entity.Dentist;
import com.dentalclinic.dentist.repository.DentistRepository;
import com.dentalclinic.patient.entity.Patient;
import com.dentalclinic.patient.repository.PatientRepository;
import com.dentalclinic.schedule.entity.DentistAvailability;
import com.dentalclinic.schedule.repository.DentistAvailabilityRepository;
import com.dentalclinic.treatment.entity.Treatment;
import com.dentalclinic.treatment.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final ClinicRepository clinicRepository;
    private final TreatmentRepository treatmentRepository;
    private final DentistAvailabilityRepository availabilityRepository;

    @Override
    public AppointmentDto createAppointment(AppointmentDto dto) {
        if (!isDentistAvailable(dto.getDentistId(), dto.getStartTime(), dto.getEndTime())) {
            throw new IllegalArgumentException("Dentist is not available at the given time or slot is already booked.");
        }

        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + dto.getClinicId()));

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));

        Dentist dentist = dentistRepository.findById(dto.getDentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with id: " + dto.getDentistId()));

        Appointment appointment = new Appointment();
        appointment.setClinic(clinic);
        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setNotes(dto.getNotes());
        appointment.setStatus(dto.getStatus() != null ? dto.getStatus() : AppointmentStatus.SCHEDULED);

        if (dto.getTreatmentId() != null) {
            Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with id: " + dto.getTreatmentId()));
            appointment.setTreatment(treatment);
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);
        return mapToDto(savedAppointment);
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (appointment.isDeleted()) {
             throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }
        return mapToDto(appointment);
    }

    @Override
    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByClinicId(Long clinicId) {
        return appointmentRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDentistId(Long dentistId) {
        return appointmentRepository.findByDentistIdAndDeletedFalse(dentistId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientIdAndDeletedFalse(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto updateAppointment(Long id, AppointmentDto dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        if (appointment.isDeleted()) {
             throw new ResourceNotFoundException("Appointment not found with id: " + id);
        }

        // Only check availability if time or dentist changed
        boolean timeChanged = !appointment.getStartTime().equals(dto.getStartTime()) || !appointment.getEndTime().equals(dto.getEndTime());
        boolean dentistChanged = !appointment.getDentist().getId().equals(dto.getDentistId());

        if (timeChanged || dentistChanged) {
             // Temporarily detach/ignore this appointment in conflict calculation (to avoid conflict with itself)
             // In a real robust system, custom query should exclude this Appointment ID.
             // But for simplicity:
             if (!isDentistAvailableForUpdate(dto.getDentistId(), dto.getStartTime(), dto.getEndTime(), id)) {
                 throw new IllegalArgumentException("Dentist is not available at the new given time.");
             }
        }

        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Dentist dentist = dentistRepository.findById(dto.getDentistId())
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found"));

        appointment.setPatient(patient);
        appointment.setDentist(dentist);
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setNotes(dto.getNotes());
        
        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }

        if (dto.getTreatmentId() != null) {
            Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Treatment not found"));
            appointment.setTreatment(treatment);
        } else {
            appointment.setTreatment(null);
        }

        Appointment updated = appointmentRepository.save(appointment);
        return mapToDto(updated);
    }

    @Override
    public AppointmentDto updateAppointmentStatus(Long id, AppointmentStatus status) {
         Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

         if (appointment.isDeleted()) {
              throw new ResourceNotFoundException("Appointment not found with id: " + id);
         }

         appointment.setStatus(status);
         return mapToDto(appointmentRepository.save(appointment));
    }

    @Override
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));

        appointment.setDeleted(true);
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public boolean isDentistAvailable(Long dentistId, LocalDateTime start, LocalDateTime end) {
        return isDentistAvailableForUpdate(dentistId, start, end, null);
    }

    private boolean isDentistAvailableForUpdate(Long dentistId, LocalDateTime start, LocalDateTime end, Long excludeAppointmentId) {
        DayOfWeek day = start.getDayOfWeek();
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();

        // 1. Check if dentist works on this day
        Optional<DentistAvailability> availabilityOpt = availabilityRepository
                .findByDentistIdAndDayOfWeekAndDeletedFalse(dentistId, day);

        if (availabilityOpt.isEmpty() || availabilityOpt.get().isDayOff()) {
            return false;
        }

        DentistAvailability availability = availabilityOpt.get();
        if (startTime.isBefore(availability.getStartTime()) || endTime.isAfter(availability.getEndTime())) {
             return false; // Out of working hours
        }

        // 2. Check overlap with other appointments
        List<Appointment> overlaps = appointmentRepository
                .findByDentistIdAndStartTimeLessThanAndEndTimeGreaterThanAndDeletedFalse(dentistId, end, start);

        if (excludeAppointmentId != null) {
            // Remove the appointment itself from overlap list if it's an update
            overlaps.removeIf(a -> a.getId().equals(excludeAppointmentId));
        }

        // Check if any active status (not CANCELLED)
        boolean hasActiveOverlap = overlaps.stream()
            .anyMatch(a -> a.getStatus() != AppointmentStatus.CANCELLED);

        return !hasActiveOverlap;
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(appointment.getId());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());

        if (appointment.getClinic() != null) dto.setClinicId(appointment.getClinic().getId());
        
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            if (appointment.getPatient().getUser() != null) {
                dto.setPatientFirstName(appointment.getPatient().getUser().getFirstName());
                dto.setPatientLastName(appointment.getPatient().getUser().getLastName());
            } else {
                 dto.setPatientFirstName(appointment.getPatient().getEmergencyContactName()); // Fallback if no user linked 
            }
        }

        if (appointment.getDentist() != null) {
            dto.setDentistId(appointment.getDentist().getId());
            if (appointment.getDentist().getUser() != null) {
                dto.setDentistFirstName(appointment.getDentist().getUser().getFirstName());
                dto.setDentistLastName(appointment.getDentist().getUser().getLastName());
            }
        }

        if (appointment.getTreatment() != null) {
            dto.setTreatmentId(appointment.getTreatment().getId());
            dto.setTreatmentName(appointment.getTreatment().getName());
        }

        return dto;
    }
}
