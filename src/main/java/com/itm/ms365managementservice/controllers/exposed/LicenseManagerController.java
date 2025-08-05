package com.itm.ms365managementservice.controllers.exposed;

import com.itm.ms365managementservice.services.LicenseManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/licenses/manager")
@RequiredArgsConstructor
public class LicenseManagerController {

    private final LicenseManagerService licenseManagerService;

    @GetMapping("/test")
    public ResponseEntity<?> testScript() {
        try {
            Map<String, Object> result = licenseManagerService.testPowerShell();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
        }
    }


    @PostMapping("/assign")
    public ResponseEntity<?> assignLicense(@RequestParam String userId, @RequestParam String skuId) {
        try {
            licenseManagerService.assignLicenseToUser(userId, skuId);
            return ResponseEntity.ok("Licence assignée");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateLicense(@RequestParam String userId, @RequestParam String licenseId) {
        try {
            licenseManagerService.deactivateUserLicense(userId, licenseId);
            return ResponseEntity.ok("Licence désactivée");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableLicenses() {
        try {
            List<String> licenses = licenseManagerService.getAvailableLicenses();
            return ResponseEntity.ok(licenses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

