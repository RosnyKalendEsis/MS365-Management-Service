package com.itm.ms365managementservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itm.ms365managementservice.entities.License;
import com.itm.ms365managementservice.entities.User;
import com.itm.ms365managementservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseManagerService {

    private final UserRepository userRepository;

    private final static String POWERSHELL_SCRIPT_PATH = "src/main/resources/scripts/powershell/";

    public void assignLicenseToUser(String userId, String skuId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        String script = POWERSHELL_SCRIPT_PATH + "assign-license.ps1";

        List<String> command = List.of("powershell.exe", "-File", script,
                "-UserPrincipalName", user.getUserPrincipalName(),
                "-SkuId", skuId);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Erreur lors de l'exécution de PowerShell : " + output);
        }

        // Ajoute la licence dans l'entité Java
        License license = new License();
        license.setAccountSkuId(skuId);
        license.setAssignedDate(ZonedDateTime.now());
        user.getLicenses().add(license);

        userRepository.save(user);
    }

    public void deactivateUserLicense(String userId, String licenseId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        License license = user.getLicenses().stream()
                .filter(l -> l.getId().equals(licenseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Licence non trouvée"));

        String script = POWERSHELL_SCRIPT_PATH + "deactivate-license.ps1";

        List<String> command = List.of("powershell.exe", "-File", script,
                "-UserPrincipalName", user.getUserPrincipalName(),
                "-SkuId", license.getAccountSkuId());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.joining("\n"));

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erreur lors de la désactivation de la licence : " + output);
        }

        // Supprimer la licence localement
        user.getLicenses().remove(license);
        userRepository.save(user);
    }

    public List<String> getAvailableLicenses() throws Exception {
        String script = POWERSHELL_SCRIPT_PATH + "list-available-licenses.ps1";

        List<String> command = List.of("powershell.exe", "-File", script);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        List<String> output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.toList());

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Erreur lors de la récupération des licences disponibles");
        }

        return output;
    }
    public Map<String, Object> testPowerShell() throws Exception {
        String script = "src/main/resources/scripts/powershell/test-ps.ps1";
        String shellCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "powershell.exe" : "pwsh";

        List<String> command = List.of(shellCommand, "-File", script);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                .lines().collect(Collectors.joining());

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Erreur PowerShell : " + output);
        }

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(output, new TypeReference<>() {});
    }

}

