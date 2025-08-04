package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.Rapport;
import com.itm.ms365managementservice.services.RapportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rapports")
public class RapportController {

    private final RapportService rapportService;

    public RapportController(RapportService rapportService) {
        this.rapportService = rapportService;
    }

    @GetMapping
    public List<Rapport> getAllRapports() {
        return rapportService.getAllRapports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rapport> getRapportById(@PathVariable String id) {
        return rapportService.getRapportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Rapport createRapport(@RequestBody Rapport rapport) {
        return rapportService.createRapport(rapport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rapport> updateRapport(@PathVariable String id, @RequestBody Rapport rapport) {
        try {
            return ResponseEntity.ok(rapportService.updateRapport(id, rapport));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRapport(@PathVariable String id) {
        rapportService.deleteRapport(id);
        return ResponseEntity.noContent().build();
    }
}

