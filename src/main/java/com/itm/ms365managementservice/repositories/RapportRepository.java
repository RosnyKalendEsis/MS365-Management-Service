package com.itm.ms365managementservice.repositories;

import com.itm.ms365managementservice.entities.Rapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, String> {
}

