package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {
    Administrator findByEmail(String email);
}
