package com.dentalclinic.clinic.entity;

import com.dentalclinic.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "clinics")
public class Clinic extends BaseEntity {

    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String taxNumber;

    private boolean active = true;

    private LocalDate subscriptionEndDate;
}
