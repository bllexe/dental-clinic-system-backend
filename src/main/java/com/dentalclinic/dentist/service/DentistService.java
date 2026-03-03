package com.dentalclinic.dentist.service;

import com.dentalclinic.dentist.dto.DentistDto;

import java.util.List;

public interface DentistService {
    DentistDto createDentist(DentistDto dentistDto);
    DentistDto getDentistById(Long id);
    List<DentistDto> getAllDentists();
    List<DentistDto> getDentistsByClinicId(Long clinicId);
    DentistDto updateDentist(Long id, DentistDto dentistDto);
    void deleteDentist(Long id);
}
