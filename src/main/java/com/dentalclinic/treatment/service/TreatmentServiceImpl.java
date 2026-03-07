package com.dentalclinic.treatment.service;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.treatment.dto.TreatmentDto;
import com.dentalclinic.treatment.entity.Treatment;
import com.dentalclinic.treatment.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentRepository treatmentRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public TreatmentDto createTreatment(TreatmentDto treatmentDto) {
        Clinic clinic = clinicRepository.findById(treatmentDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + treatmentDto.getClinicId()));

        Treatment treatment = new Treatment();
        treatment.setClinic(clinic);
        treatment.setName(treatmentDto.getName());
        treatment.setDescription(treatmentDto.getDescription());
        treatment.setDefaultPrice(treatmentDto.getDefaultPrice());
        treatment.setEstimatedDurationMinutes(treatmentDto.getEstimatedDurationMinutes());

        Treatment savedTreatment = treatmentRepository.save(treatment);
        return mapToDto(savedTreatment);
    }

    @Override
    public TreatmentDto getTreatmentById(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with id: " + id));

        if (treatment.isDeleted()) {
            throw new ResourceNotFoundException("Treatment not found with id: " + id);
        }

        return mapToDto(treatment);
    }

    @Override
    public List<TreatmentDto> getAllTreatments() {
        return treatmentRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreatmentDto> getTreatmentsByClinicId(Long clinicId) {
        return treatmentRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TreatmentDto updateTreatment(Long id, TreatmentDto treatmentDto) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with id: " + id));

        if (treatment.isDeleted()) {
             throw new ResourceNotFoundException("Treatment not found with id: " + id);
        }

        Clinic clinic = clinicRepository.findById(treatmentDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + treatmentDto.getClinicId()));

        treatment.setClinic(clinic);
        treatment.setName(treatmentDto.getName());
        treatment.setDescription(treatmentDto.getDescription());
        treatment.setDefaultPrice(treatmentDto.getDefaultPrice());
        treatment.setEstimatedDurationMinutes(treatmentDto.getEstimatedDurationMinutes());

        Treatment updatedTreatment = treatmentRepository.save(treatment);
        return mapToDto(updatedTreatment);
    }

    @Override
    public void deleteTreatment(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Treatment not found with id: " + id));

        treatment.setDeleted(true);
        treatmentRepository.save(treatment);
    }

    private TreatmentDto mapToDto(Treatment treatment) {
        TreatmentDto dto = new TreatmentDto();
        dto.setId(treatment.getId());
        dto.setName(treatment.getName());
        dto.setDescription(treatment.getDescription());
        dto.setDefaultPrice(treatment.getDefaultPrice());
        dto.setEstimatedDurationMinutes(treatment.getEstimatedDurationMinutes());
        
        if (treatment.getClinic() != null) {
            dto.setClinicId(treatment.getClinic().getId());
        }

        return dto;
    }
}
