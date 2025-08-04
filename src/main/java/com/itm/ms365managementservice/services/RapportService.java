package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.Rapport;
import com.itm.ms365managementservice.repositories.RapportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RapportService {

    private final RapportRepository rapportRepository;

    public RapportService(RapportRepository rapportRepository) {
        this.rapportRepository = rapportRepository;
    }

    public List<Rapport> getAllRapports() {
        return rapportRepository.findAll();
    }

    public Optional<Rapport> getRapportById(String id) {
        return rapportRepository.findById(id);
    }

    public Rapport createRapport(Rapport rapport) {
        return rapportRepository.save(rapport);
    }

    public Rapport updateRapport(String id, Rapport updatedRapport) {
        if (rapportRepository.existsById(id)) {
            updatedRapport.setId(id);
            return rapportRepository.save(updatedRapport);
        }
        throw new RuntimeException("Rapport not found with id: " + id);
    }

    public void deleteRapport(String id) {
        rapportRepository.deleteById(id);
    }
}

