package com.dentalclinic.dentist.service;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.dentist.dto.DentistDto;
import com.dentalclinic.dentist.entity.Dentist;
import com.dentalclinic.dentist.repository.DentistRepository;
import com.dentalclinic.user.entity.User;
import com.dentalclinic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DentistServiceImpl implements DentistService {

    private final DentistRepository dentistRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public DentistDto createDentist(DentistDto dentistDto) {
        User user = userRepository.findById(dentistDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + dentistDto.getUserId()));

        Clinic clinic = clinicRepository.findById(dentistDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + dentistDto.getClinicId()));

        Dentist dentist = new Dentist();
        dentist.setUser(user);
        dentist.setClinic(clinic);
        dentist.setSpecialization(dentistDto.getSpecialization());
        dentist.setLicenseNumber(dentistDto.getLicenseNumber());
        dentist.setBiography(dentistDto.getBiography());

        Dentist savedDentist = dentistRepository.save(dentist);
        return mapToDto(savedDentist);
    }

    @Override
    public DentistDto getDentistById(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with id: " + id));
        
        if (dentist.isDeleted()) {
            throw new ResourceNotFoundException("Dentist not found with id: " + id);
        }
        
        return mapToDto(dentist);
    }

    @Override
    public List<DentistDto> getAllDentists() {
        return dentistRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DentistDto> getDentistsByClinicId(Long clinicId) {
        return dentistRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DentistDto updateDentist(Long id, DentistDto dentistDto) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with id: " + id));

        if (dentist.isDeleted()) {
             throw new ResourceNotFoundException("Dentist not found with id: " + id);
        }

        Clinic clinic = clinicRepository.findById(dentistDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + dentistDto.getClinicId()));

        dentist.setClinic(clinic);
        dentist.setSpecialization(dentistDto.getSpecialization());
        dentist.setLicenseNumber(dentistDto.getLicenseNumber());
        dentist.setBiography(dentistDto.getBiography());

        Dentist updatedDentist = dentistRepository.save(dentist);
        return mapToDto(updatedDentist);
    }

    @Override
    public void deleteDentist(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dentist not found with id: " + id));

        // Soft delete
        dentist.setDeleted(true);
        dentistRepository.save(dentist);
    }

    private DentistDto mapToDto(Dentist dentist) {
        DentistDto dto = new DentistDto();
        dto.setId(dentist.getId());
        dto.setSpecialization(dentist.getSpecialization());
        dto.setLicenseNumber(dentist.getLicenseNumber());
        dto.setBiography(dentist.getBiography());
        
        if (dentist.getClinic() != null) {
            dto.setClinicId(dentist.getClinic().getId());
        }

        if (dentist.getUser() != null) {
            dto.setUserId(dentist.getUser().getId());
            dto.setFirstName(dentist.getUser().getFirstName());
            dto.setLastName(dentist.getUser().getLastName());
            dto.setEmail(dentist.getUser().getEmail());
        }

        return dto;
    }
}
