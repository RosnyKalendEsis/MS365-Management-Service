package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, String> {

    // Trouver un admin par email
    Administrator findByEmail(String email);

    // Vérifier l'existence d'un admin par email
    boolean existsByEmail(String email);
}

