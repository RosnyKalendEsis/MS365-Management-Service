package com.itm.ms365managementservice.controllers.exposed;

import com.itm.ms365managementservice.services.PowerShellService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/powershell")
public class PowerShellController {
    private final PowerShellService powerShellService;

    @PostMapping("/run")
    public String runCommand(@RequestBody String command) {
        try {
            powerShellService.sendCommand(command);
            return "Commande envoyée avec succès : " + command;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    @PostMapping("/stop")
    public String stop() {
        powerShellService.stopPowerShellSession();
        return "Session PowerShell terminée.";
    }

    @PostMapping("/run-script")
    public String runScript(@RequestBody String scriptPath) {
        try {
            powerShellService.sendScriptFile(scriptPath);
            return "Script exécuté avec succès : " + scriptPath;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}

