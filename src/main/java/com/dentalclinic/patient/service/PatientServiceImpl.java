package com.dentalclinic.patient.service;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.patient.dto.PatientDto;
import com.dentalclinic.patient.entity.Patient;
import com.dentalclinic.patient.repository.PatientRepository;
import com.dentalclinic.user.entity.User;
import com.dentalclinic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public PatientDto createPatient(PatientDto patientDto) {
        Clinic clinic = clinicRepository.findById(patientDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + patientDto.getClinicId()));

        Patient patient = new Patient();
        patient.setClinic(clinic);
        patient.setIdentityNumber(patientDto.getIdentityNumber());
        patient.setBirthDate(patientDto.getBirthDate());
        patient.setGender(patientDto.getGender());
        patient.setAddress(patientDto.getAddress());
        patient.setEmergencyContactName(patientDto.getEmergencyContactName());
        patient.setEmergencyContactPhone(patientDto.getEmergencyContactPhone());

        // Optional linking to an existing system user (maybe patient uses mobile app)
        if (patientDto.getUserId() != null) {
            User user = userRepository.findById(patientDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + patientDto.getUserId()));
            patient.setUser(user);
        }

        Patient savedPatient = patientRepository.save(patient);
        return mapToDto(savedPatient);
    }

    @Override
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (patient.isDeleted()) {
             throw new ResourceNotFoundException("Patient not found with id: " + id);
        }

        return mapToDto(patient);
    }

    @Override
    public List<PatientDto> getAllPatients() {
        return patientRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientDto> getPatientsByClinicId(Long clinicId) {
        return patientRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDto updatePatient(Long id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        if (patient.isDeleted()) {
             throw new ResourceNotFoundException("Patient not found with id: " + id);
        }

        Clinic clinic = clinicRepository.findById(patientDto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + patientDto.getClinicId()));

        patient.setClinic(clinic);
        patient.setIdentityNumber(patientDto.getIdentityNumber());
        patient.setBirthDate(patientDto.getBirthDate());
        patient.setGender(patientDto.getGender());
        patient.setAddress(patientDto.getAddress());
        patient.setEmergencyContactName(patientDto.getEmergencyContactName());
        patient.setEmergencyContactPhone(patientDto.getEmergencyContactPhone());

        // You wouldn't typically change the underlying user on an existing patient, 
        // but we can allow linking if missing
        if (patient.getUser() == null && patientDto.getUserId() != null) {
             User user = userRepository.findById(patientDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + patientDto.getUserId()));
             patient.setUser(user);
        }

        Patient updatedPatient = patientRepository.save(patient);
        return mapToDto(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

        // Soft delete
        patient.setDeleted(true);
        patientRepository.save(patient);
    }

    private PatientDto mapToDto(Patient patient) {
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setIdentityNumber(patient.getIdentityNumber());
        dto.setBirthDate(patient.getBirthDate());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        
        if (patient.getClinic() != null) {
            dto.setClinicId(patient.getClinic().getId());
        }

        if (patient.getUser() != null) {
            dto.setUserId(patient.getUser().getId());
            dto.setFirstName(patient.getUser().getFirstName());
            dto.setLastName(patient.getUser().getLastName());
            dto.setEmail(patient.getUser().getEmail());
            dto.setPhoneNumber(patient.getUser().getPhoneNumber());
        }

        return dto;
    }
}
