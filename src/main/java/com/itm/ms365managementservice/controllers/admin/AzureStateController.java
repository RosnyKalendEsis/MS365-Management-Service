package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.AzureState;
import com.itm.ms365managementservice.services.AzureStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/azure-state")
public class AzureStateController {

    private final AzureStateService azureStateService;

    @Autowired
    public AzureStateController(AzureStateService azureStateService) {
        this.azureStateService = azureStateService;
    }

    // ✅ Créer ou mettre à jour un AzureState
    @PostMapping
    public ResponseEntity<AzureState> createOrUpdate(@RequestBody AzureState azureState) {
        AzureState saved = azureStateService.save(azureState);
        return ResponseEntity.ok(saved);
    }

    // ✅ Récupérer tous les AzureState
    @GetMapping
    public ResponseEntity<List<AzureState>> getAll() {
        return ResponseEntity.ok(azureStateService.findAll());
    }

    // ✅ Récupérer un AzureState par ID
    @GetMapping("/{id}")
    public ResponseEntity<AzureState> getById(@PathVariable String id) {
        return azureStateService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Supprimer un AzureState par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (azureStateService.findById(id).isPresent()) {
            azureStateService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}