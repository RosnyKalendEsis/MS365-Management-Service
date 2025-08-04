package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.repositories.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final AdministratorRepository administratorRepository;

    // Créer un administrateur
    public Administrator createAdministrator(Administrator administrator) {
        return administratorRepository.save(administrator);
    }

    // Mettre à jour un administrateur
    public Administrator updateAdministrator(String id, Administrator updatedAdmin) {
        Optional<Administrator> optionalAdmin = administratorRepository.findById(id);
        if (optionalAdmin.isPresent()) {
            Administrator existingAdmin = optionalAdmin.get();
            existingAdmin.setEmail(updatedAdmin.getEmail());
            existingAdmin.setPwd(updatedAdmin.getPwd());
            existingAdmin.setRole(updatedAdmin.getRole());
            return administratorRepository.save(existingAdmin);
        }
        throw new RuntimeException("Administrator not found with id: " + id);
    }

    // Supprimer un administrateur
    public void deleteAdministrator(String id) {
        administratorRepository.deleteById(id);
    }

    // Obtenir un administrateur par ID
    public Optional<Administrator> getAdministratorById(String id) {
        return administratorRepository.findById(id);
    }

    // Obtenir un administrateur par email
    public Administrator getAdministratorByEmail(String email) {
        return administratorRepository.findByEmail(email);
    }

    // Obtenir tous les administrateurs
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    // Vérifier s'il existe un administrateur avec l'email donné
    public boolean existsByEmail(String email) {
        return administratorRepository.existsByEmail(email);
    }
}