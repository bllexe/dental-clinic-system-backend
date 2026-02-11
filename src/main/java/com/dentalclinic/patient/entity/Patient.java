package com.dentalclinic.patient.entity;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.common.entity.BaseEntity;
import com.dentalclinic.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // Optional: If patient is also a system user

    // Basic details if not linked to a User, or additional to User
    private String identityNumber; // TC Kimlik No etc.
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String emergencyContactName;
    private String emergencyContactPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;
}
