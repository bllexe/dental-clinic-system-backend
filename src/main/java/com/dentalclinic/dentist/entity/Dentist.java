package com.dentalclinic.dentist.entity;

import com.dentalclinic.clinic.entity.Clinic;
import com.dentalclinic.common.entity.BaseEntity;
import com.dentalclinic.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dentists")
public class Dentist extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Dentist must be a user to login

    private String specialization; // e.g., Orthodontist, Surgeon
    private String licenseNumber;

    @Column(length = 1000)
    private String biography;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

}
