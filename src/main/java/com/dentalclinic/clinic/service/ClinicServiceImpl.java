package com.dentalclinic.clinic.service;

import com.dentalclinic.clinic.dto.ClinicDto;
import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicServiceImpl implements ClinicService {

    private final ClinicRepository clinicRepository;

    @Override
    public ClinicDto createClinic(ClinicDto clinicDto) {
        Clinic clinic = mapToEntity(clinicDto);
        Clinic newClinic = clinicRepository.save(clinic);
        return mapToDto(newClinic);
    }

    @Override
    public ClinicDto getClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));
        return mapToDto(clinic);
    }

    @Override
    public List<ClinicDto> getAllClinics() {
        return clinicRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClinicDto> getActiveClinics() {
        return clinicRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ClinicDto updateClinic(Long id, ClinicDto clinicDto) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));

        clinic.setName(clinicDto.getName());
        clinic.setAddress(clinicDto.getAddress());
        clinic.setPhoneNumber(clinicDto.getPhoneNumber());
        clinic.setEmail(clinicDto.getEmail());
        clinic.setTaxNumber(clinicDto.getTaxNumber());
        clinic.setActive(clinicDto.isActive());
        clinic.setSubscriptionEndDate(clinicDto.getSubscriptionEndDate());

        Clinic updatedClinic = clinicRepository.save(clinic);
        return mapToDto(updatedClinic);
    }

    @Override
    public void deleteClinic(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));
        
        // Soft delete
        clinic.setDeleted(true);
        clinic.setActive(false);
        clinicRepository.save(clinic);
    }

    // Manual mapping for now
    private ClinicDto mapToDto(Clinic clinic) {
        ClinicDto dto = new ClinicDto();
        dto.setId(clinic.getId());
        dto.setName(clinic.getName());
        dto.setAddress(clinic.getAddress());
        dto.setPhoneNumber(clinic.getPhoneNumber());
        dto.setEmail(clinic.getEmail());
        dto.setTaxNumber(clinic.getTaxNumber());
        dto.setActive(clinic.isActive());
        dto.setSubscriptionEndDate(clinic.getSubscriptionEndDate());
        return dto;
    }

    private Clinic mapToEntity(ClinicDto dto) {
        Clinic clinic = new Clinic();
        clinic.setName(dto.getName());
        clinic.setAddress(dto.getAddress());
        clinic.setPhoneNumber(dto.getPhoneNumber());
        clinic.setEmail(dto.getEmail());
        clinic.setTaxNumber(dto.getTaxNumber());
        clinic.setActive(dto.isActive());
        clinic.setSubscriptionEndDate(dto.getSubscriptionEndDate());
        return clinic;
    }
}
