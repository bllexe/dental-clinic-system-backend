package com.dentalclinic.clinic.service;

import com.dentalclinic.clinic.dto.ClinicDto;
import java.util.List;

public interface ClinicService {
    ClinicDto createClinic(ClinicDto clinicDto);
    ClinicDto getClinicById(Long id);
    List<ClinicDto> getAllClinics();
    List<ClinicDto> getActiveClinics();
    ClinicDto updateClinic(Long id, ClinicDto clinicDto);
    void deleteClinic(Long id);
}
