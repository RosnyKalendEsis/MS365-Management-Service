package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.AzureState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AzureStateRepository extends JpaRepository<AzureState, String> {
    AzureState findTopByOrderByIdDesc();
}