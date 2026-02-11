package com.dentalclinic.appointment.entity;

import com.dentalclinic.appointment.enums.AppointmentStatus;
import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.common.entity.BaseEntity;
import com.dentalclinic.dentist.entity.Dentist;
import com.dentalclinic.patient.entity.Patient;
import com.dentalclinic.treatment.entity.Treatment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private Dentist dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String notes;

    // Primary treatment (could have multiple, but keeping simple for now or adding
    // ManyToMany later)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;
}
