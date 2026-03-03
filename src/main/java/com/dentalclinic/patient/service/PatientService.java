package com.dentalclinic.patient.service;

import com.dentalclinic.patient.dto.PatientDto;

import java.util.List;

public interface PatientService {
    PatientDto createPatient(PatientDto patientDto);
    PatientDto getPatientById(Long id);
    List<PatientDto> getAllPatients();
    List<PatientDto> getPatientsByClinicId(Long clinicId);
    PatientDto updatePatient(Long id, PatientDto patientDto);
    void deletePatient(Long id);
}
