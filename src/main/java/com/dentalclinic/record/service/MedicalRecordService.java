package com.dentalclinic.record.service;

import com.dentalclinic.record.dto.MedicalRecordDto;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordDto createRecord(MedicalRecordDto dto);
    MedicalRecordDto getRecordById(Long id);
    List<MedicalRecordDto> getAllRecords();
    List<MedicalRecordDto> getRecordsByClinicId(Long clinicId);
    List<MedicalRecordDto> getRecordsByPatientId(Long patientId);
    List<MedicalRecordDto> getRecordsByAppointmentId(Long appointmentId);
    MedicalRecordDto updateRecord(Long id, MedicalRecordDto dto);
    void deleteRecord(Long id);
}
