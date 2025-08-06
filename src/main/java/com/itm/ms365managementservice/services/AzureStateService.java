package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.AzureState;
import com.itm.ms365managementservice.repositories.AzureStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AzureStateService {
    private final AzureStateRepository azureStateRepository;

    public AzureState save(AzureState state) {
        return azureStateRepository.save(state);
    }

    public Optional<AzureState> findById(String id) {
        return azureStateRepository.findById(id);
    }

    public List<AzureState> findAll() {
        return azureStateRepository.findAll();
    }

    public void deleteById(String id) {
        azureStateRepository.deleteById(id);
    }
}
