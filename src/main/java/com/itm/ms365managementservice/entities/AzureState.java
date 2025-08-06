package com.itm.ms365managementservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AzureState {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private boolean isConnected;
    private String code;
    private String callback;
}
