package com.dentalclinic.treatment.service;

import com.dentalclinic.treatment.dto.TreatmentDto;

import java.util.List;

public interface TreatmentService {
    TreatmentDto createTreatment(TreatmentDto treatmentDto);
    TreatmentDto getTreatmentById(Long id);
    List<TreatmentDto> getAllTreatments();
    List<TreatmentDto> getTreatmentsByClinicId(Long clinicId);
    TreatmentDto updateTreatment(Long id, TreatmentDto treatmentDto);
    void deleteTreatment(Long id);
}
