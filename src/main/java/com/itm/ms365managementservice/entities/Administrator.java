package com.itm.ms365managementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@PrimaryKeyJoinColumn(name = "id") // même clé primaire que User
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends User {

    private String email;
    private String pwd;
    private ZonedDateTime lastLoginDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Role role;
}
