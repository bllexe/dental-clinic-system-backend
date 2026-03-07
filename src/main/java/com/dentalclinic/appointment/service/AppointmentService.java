package com.dentalclinic.appointment.service;

import com.dentalclinic.appointment.dto.AppointmentDto;
import com.dentalclinic.appointment.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDto createAppointment(AppointmentDto dto);
    AppointmentDto getAppointmentById(Long id);
    List<AppointmentDto> getAllAppointments();
    List<AppointmentDto> getAppointmentsByClinicId(Long clinicId);
    List<AppointmentDto> getAppointmentsByDentistId(Long dentistId);
    List<AppointmentDto> getAppointmentsByPatientId(Long patientId);
    AppointmentDto updateAppointment(Long id, AppointmentDto dto);
    AppointmentDto updateAppointmentStatus(Long id, AppointmentStatus status);
    
    // Future possibility: find available slots, but for now we'll stick to basic conflict checking in creation
    boolean isDentistAvailable(Long dentistId, LocalDateTime start, LocalDateTime end);
    
    void deleteAppointment(Long id);
}
