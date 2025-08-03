package com.itm.ms365managementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Administrator {

    @Id
    private String id = UUID.randomUUID().toString();

    private String email;
    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Role role;
}
