package com.itm.ms365managementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String displayName;
    private String userPrincipalName;
    private String jobTitle;
    private String department;
    private String officeLocation;
    private String phoneNumber;
    private boolean isLicensed;
    private boolean isLicenseExpired;
    private ZonedDateTime createdDate;
    private String status;

    @Embedded
    private Manager manager;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<License> licenses;
}
