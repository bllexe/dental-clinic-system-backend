package com.dentalclinic.record.service;

import com.dentalclinic.appointment.entity.Appointment;
import com.dentalclinic.appointment.repository.AppointmentRepository;
import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.clinic.repository.ClinicRepository;
import com.dentalclinic.common.exception.ResourceNotFoundException;
import com.dentalclinic.patient.entity.Patient;
import com.dentalclinic.patient.repository.PatientRepository;
import com.dentalclinic.record.dto.MedicalRecordDto;
import com.dentalclinic.record.entity.MedicalRecord;
import com.dentalclinic.record.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository recordRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public MedicalRecordDto createRecord(MedicalRecordDto dto) {
        Clinic clinic = clinicRepository.findById(dto.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found"));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        MedicalRecord record = new MedicalRecord();
        record.setClinic(clinic);
        record.setPatient(patient);
        record.setDiagnosis(dto.getDiagnosis());
        record.setTreatmentNotes(dto.getTreatmentNotes());
        record.setPrescription(dto.getPrescription());

        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            record.setAppointment(appointment);
        }

        MedicalRecord savedRecord = recordRepository.save(record);
        return mapToDto(savedRecord);
    }

    @Override
    public MedicalRecordDto getRecordById(Long id) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
                
        if (record.isDeleted()) {
           throw new ResourceNotFoundException("Medical record not found.");
        }
        
        return mapToDto(record);
    }

    @Override
    public List<MedicalRecordDto> getAllRecords() {
        return recordRepository.findByDeletedFalse().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDto> getRecordsByClinicId(Long clinicId) {
        return recordRepository.findByClinicIdAndDeletedFalse(clinicId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDto> getRecordsByPatientId(Long patientId) {
        return recordRepository.findByPatientIdAndDeletedFalse(patientId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDto> getRecordsByAppointmentId(Long appointmentId) {
        return recordRepository.findByAppointmentIdAndDeletedFalse(appointmentId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDto updateRecord(Long id, MedicalRecordDto dto) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        if (record.isDeleted()) {
           throw new ResourceNotFoundException("Medical record not found.");
        }
        
        record.setDiagnosis(dto.getDiagnosis());
        record.setTreatmentNotes(dto.getTreatmentNotes());
        record.setPrescription(dto.getPrescription());

        // Update appointment if needed. But usually, it doesn't change after creation.
        if (dto.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            record.setAppointment(appointment);
        }

        MedicalRecord updated = recordRepository.save(record);
        return mapToDto(updated);
    }

    @Override
    public void deleteRecord(Long id) {
        MedicalRecord record = recordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));

        record.setDeleted(true);
        recordRepository.save(record);
    }

    private MedicalRecordDto mapToDto(MedicalRecord record) {
        MedicalRecordDto dto = new MedicalRecordDto();
        dto.setId(record.getId());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setTreatmentNotes(record.getTreatmentNotes());
        dto.setPrescription(record.getPrescription());

        if (record.getClinic() != null) {
            dto.setClinicId(record.getClinic().getId());
        }

        if (record.getPatient() != null) {
            dto.setPatientId(record.getPatient().getId());
            if (record.getPatient().getUser() != null) {
                 dto.setPatientFirstName(record.getPatient().getUser().getFirstName());
                 dto.setPatientLastName(record.getPatient().getUser().getLastName());
            } else {
                 dto.setPatientFirstName(record.getPatient().getEmergencyContactName());
            }
        }

        if (record.getAppointment() != null) {
            dto.setAppointmentId(record.getAppointment().getId());
        }

        return dto;
    }
}
