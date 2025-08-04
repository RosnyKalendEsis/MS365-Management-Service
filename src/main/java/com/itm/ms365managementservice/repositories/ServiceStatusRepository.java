package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceStatusRepository extends JpaRepository<ServiceStatus, String> {
}
