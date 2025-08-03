package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.repositories.AdministratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdministratorService {

    private final AdministratorRepository repository;

    public List<Administrator> getAll() {
        return repository.findAll();
    }

    public Administrator getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Administrator create(Administrator administrator) {
        return repository.save(administrator);
    }

    public Administrator update(String id, Administrator u) {
        return repository.findById(id).map(existing -> {
            existing.setEmail(u.getEmail());
            existing.setPwd(u.getPwd());
            existing.setRole(u.getRole());
            return repository.save(existing);
        }).orElse(null);
    }

    public Administrator findByEmail(String email){
        return repository.findByEmail(email);
    }


    public void delete(String id) {
        repository.deleteById(id);
    }
}
