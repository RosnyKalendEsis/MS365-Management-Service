package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, String> {
}

