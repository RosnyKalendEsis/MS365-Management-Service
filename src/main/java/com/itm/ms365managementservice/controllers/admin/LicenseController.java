package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping
    public List<License> getAllLicenses() {
        return licenseService.getAllLicenses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<License> getLicenseById(@PathVariable String id) {
        return licenseService.getLicenseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<License> createLicense(@RequestBody License license) {
        return ResponseEntity.ok(licenseService.createLicense(license));
    }

    @PutMapping("/{id}")
    public ResponseEntity<License> updateLicense(@PathVariable String id, @RequestBody License updatedLicense) {
        return ResponseEntity.ok(licenseService.updateLicense(id, updatedLicense));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable String id) {
        licenseService.deleteLicense(id);
        return ResponseEntity.noContent().build();
    }
}
