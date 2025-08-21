package com.itm.ms365managementservice.controllers.exposed;

import com.itm.ms365managementservice.entities.PowerShellCommandDTO;
import com.itm.ms365managementservice.services.PowerShellService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/powershell")
public class
PowerShellController {
    private final PowerShellService powerShellService;
    private final static String POWERSHELL_SCRIPT_PATH = "src/main/resources/scripts/powershell/";

    @PostMapping("/run")
    public Map<String, Object> runCommand(@RequestBody PowerShellCommandDTO dto) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Exécuter la commande PowerShell
            powerShellService.sendCommand(dto.getCommand());

            // Lire le contenu du fichier JSON
            File file = new File(POWERSHELL_SCRIPT_PATH+"response.json");
            if (!file.exists() || file.length() == 0) {
                response.put("data", null);
            } else {
                String json = Files.readString(Paths.get(POWERSHELL_SCRIPT_PATH+"response.json"));
                response.put("data", json.isBlank() ? null : json);
            }

        } catch (Exception e) {
            response.put("error", "Erreur : " + e.getMessage());
        }

        return response;
    }

    @PostMapping("check-user")
    public Map<String, Object> runCommandCheckUser(@RequestBody PowerShellCommandDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Exécuter la commande PowerShell
            powerShellService.sendCommand(dto.getCommand());

            // Attendre 5 secondes (5000 ms)
            Thread.sleep(5000);

            // Lire le contenu du fichier JSON
            String json = Files.readString(Paths.get(POWERSHELL_SCRIPT_PATH+"ExistUser.json"));
            response.put("data", json.isBlank() ? null : json);

        } catch (Exception e) {
            response.put("error", "Erreur : " + e.getMessage());
        }

        return response;
    }

    @PostMapping("get-users")
    public Map<String, Object> runCommandGetUsers(@RequestBody PowerShellCommandDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Exécuter la commande PowerShell
            powerShellService.sendCommand(dto.getCommand());

            // Attendre 5 secondes (5000 ms)
            Thread.sleep(5000);

            // Lire le contenu du fichier JSON
            String json = Files.readString(Paths.get(POWERSHELL_SCRIPT_PATH+"azure_users.json"));
            response.put("data", json.isBlank() ? null : json);

        } catch (Exception e) {
            response.put("error", "Erreur : " + e.getMessage());
        }

        return response;
    }

    @PostMapping("get-licences")
    public Map<String, Object> runCommandGetSkUI(@RequestBody PowerShellCommandDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Exécuter la commande PowerShell
            powerShellService.sendCommand(dto.getCommand());

            // Attendre 5 secondes (5000 ms)
            Thread.sleep(5000);

            // Lire le contenu du fichier JSON
            String json = Files.readString(Paths.get(POWERSHELL_SCRIPT_PATH+"azure_licences.json"));
            response.put("data", json.isBlank() ? null : json);

        } catch (Exception e) {
            response.put("error", "Erreur : " + e.getMessage());
        }

        return response;
    }



    @PostMapping("/stop")
    public String stop() {
        powerShellService.stopPowerShellSession();
        return "Session PowerShell terminée.";
    }

    @PostMapping("/run-script")
    public String runScript(@RequestBody String scriptPath) {
        try {
            String jsonResult = powerShellService.runScript(scriptPath);
            return "Script exécuté avec succès : " + jsonResult;
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}

