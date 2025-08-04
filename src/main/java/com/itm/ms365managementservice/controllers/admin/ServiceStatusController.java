package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.ServiceStatus;
import com.itm.ms365managementservice.services.ServiceStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-statuses")
public class ServiceStatusController {

    private final ServiceStatusService serviceStatusService;

    @Autowired
    public ServiceStatusController(ServiceStatusService serviceStatusService) {
        this.serviceStatusService = serviceStatusService;
    }

    @GetMapping
    public List<ServiceStatus> getAllServiceStatuses() {
        return serviceStatusService.getAllStatuses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceStatus> getById(@PathVariable String id) {
        return serviceStatusService.getStatusById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceStatus> create(@RequestBody ServiceStatus status) {
        return ResponseEntity.ok(serviceStatusService.createStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceStatus> update(@PathVariable String id, @RequestBody ServiceStatus updated) {
        return ResponseEntity.ok(serviceStatusService.updateStatus(id, updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceStatusService.deleteStatus(id);
        return ResponseEntity.noContent().build();
    }
}
