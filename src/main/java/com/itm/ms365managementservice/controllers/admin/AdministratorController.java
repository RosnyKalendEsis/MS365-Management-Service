package com.itm.ms365managementservice.controllers.admin;


import com.itm.ms365managementservice.entities.Administrator;
import com.itm.ms365managementservice.services.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrators")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    // 🔹 Créer un nouvel administrateur
    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator administrator) {
        Administrator created = administratorService.createAdministrator(administrator);
        return ResponseEntity.ok(created);
    }

    // 🔹 Mettre à jour un administrateur
    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(
            @PathVariable String id,
            @RequestBody Administrator updatedAdmin
    ) {
        Administrator updated = administratorService.updateAdministrator(id, updatedAdmin);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Supprimer un administrateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable String id) {
        administratorService.deleteAdministrator(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Obtenir un administrateur par ID
    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable String id) {
        return administratorService.getAdministratorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔹 Obtenir tous les administrateurs
    @GetMapping
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        return ResponseEntity.ok(administratorService.getAllAdministrators());
    }

    // 🔹 Vérifier l'existence d'un administrateur par email
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(administratorService.existsByEmail(email));
    }
}

