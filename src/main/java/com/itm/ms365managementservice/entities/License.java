package com.itm.ms365managementservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String accountSkuId;

    private ZonedDateTime assignedDate;
    private ZonedDateTime expiryDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "license_id")
    private List<ServiceStatus> serviceStatus;
}
