package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.ServiceStatus;
import com.itm.ms365managementservice.repositories.ServiceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceStatusService {

    private final ServiceStatusRepository serviceStatusRepository;

    @Autowired
    public ServiceStatusService(ServiceStatusRepository serviceStatusRepository) {
        this.serviceStatusRepository = serviceStatusRepository;
    }

    public List<ServiceStatus> getAllStatuses() {
        return serviceStatusRepository.findAll();
    }

    public Optional<ServiceStatus> getStatusById(String id) {
        return serviceStatusRepository.findById(id);
    }

    public ServiceStatus createStatus(ServiceStatus status) {
        return serviceStatusRepository.save(status);
    }

    public ServiceStatus updateStatus(String id, ServiceStatus updatedStatus) {
        updatedStatus.setId(id);
        return serviceStatusRepository.save(updatedStatus);
    }

    public void deleteStatus(String id) {
        serviceStatusRepository.deleteById(id);
    }
}
