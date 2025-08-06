package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.AzureState;
import com.itm.ms365managementservice.repositories.AzureStateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerShellService {

    private Process process;
    private BufferedWriter commandWriter;
    private BufferedReader commandReader;
    private final AzureStateRepository azureStateRepository;
    private final static String POWERSHELL_SCRIPT_PATH = "src/main/resources/scripts/powershell/";

    @PostConstruct
    public void startPowerShellSession() {
        try {
            ProcessBuilder builder = new ProcessBuilder("pwsh", "-NoExit", "-Command", "-");
            builder.redirectErrorStream(true); // Redirige stderr vers stdout
            process = builder.start();

            commandWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            commandReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<AzureState> dbAzureStates = azureStateRepository.findAll();

            // Lecture des logs en arrière-plan
            new Thread(() -> {
                String line;
                String url = null;
                String code = null;
                boolean isConnected = false;

                try {
                    while ((line = commandReader.readLine()) != null) {
                        System.out.println("[POWERSHELL] " + line);

                        // 🔗 Récupération de l'URL
                        if (line.contains("https://microsoft.com/devicelogin")) {
                            url = "https://microsoft.com/devicelogin";
                            System.out.println("🔗 URL d'authentification : " + url);
                        }

                        // 🔐 Récupération du code
                        if (line.matches(".*\\b[A-Z0-9]{8,12}\\b.*")) {
                            String[] words = line.split("\\s+");
                            for (String wordCandidate : words) {
                                if (wordCandidate.matches("\\b[A-Z0-9]{8,12}\\b")) {
                                    code = wordCandidate;
                                    System.out.println("🔐 Code d'authentification : " + code);
                                    break;
                                }
                            }
                        }

                        // ✅ Enregistrement de l'état si code et URL trouvés
                        if (url != null && code != null) {
                            AzureState azureState = new AzureState();
                            if(!dbAzureStates.isEmpty()){
                                azureState.setId(dbAzureStates.get(0).getId());
                            }
                            azureState.setConnected(false);
                            azureState.setCode(code);
                            azureState.setCallback(url);
                            azureStateRepository.save(azureState);
                        }

                        // ✅ Détection de la connexion réussie à AzureAD
                        if (line.matches(".*@.*\\s+AzureCloud\\s+[a-f0-9\\-]{36}.*")) {
                            System.out.println("✅ Connexion à AzureAD détectée.");
                            AzureState azureState = new AzureState();
                            if(!dbAzureStates.isEmpty()){
                                azureState.setId(dbAzureStates.get(0).getId());
                            }
                            azureState.setConnected(true);
                            azureState.setCode(null);
                            azureState.setCallback(null);
                            azureStateRepository.save(azureState);
                            isConnected = true;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            System.out.println("PowerShell démarré.");
            Thread.sleep(1000);

            // ✅ Commandes initiales
            sendCommand("Import-Module AzureAD.Standard.Preview");
            sendCommand("Connect-AzureAD");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void sendCommand(String command) throws IOException {
        if (commandWriter != null) {
            commandWriter.write(command);
            commandWriter.newLine(); // simule le retour chariot
            commandWriter.flush();
        }
    }

    public void stopPowerShellSession() {
        if (process != null) {
            process.destroy();
        }
    }

    public void sendScriptFile(String scriptPath) throws IOException {
        if (commandWriter == null) {
            throw new IllegalStateException("La session PowerShell n’est pas démarrée.");
        }

        // Lire tout le contenu du fichier
        List<String> lines = Files.readAllLines(Paths.get(scriptPath));

        for (String line : lines) {
            commandWriter.write(line);
            commandWriter.newLine();
        }

        commandWriter.flush(); // très important
    }
}

