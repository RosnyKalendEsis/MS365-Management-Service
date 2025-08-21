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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

            // Lecture des logs en arrière-plan
            new Thread(() -> {
                String line;
                String url = null;
                String code = null;
                boolean isConnected = false;

                try {
                    while ((line = commandReader.readLine()) != null) {
                        List<AzureState> dbAzureStates = azureStateRepository.findAll();
                        System.out.println("[POWERSHELL] " + line);

                        // 🔗 Récupération de l'URL
                        if (line.contains("https://microsoft.com/devicelogin")) {
                            url = "https://microsoft.com/devicelogin";
                            System.out.println("🔗 URL d'authentification : " + url);
                        }

                        // 🔐 Récupération plus précise du code
                        Pattern precisePattern = Pattern.compile("the code\\s+([A-Z0-9]{8,12})\\s+to authenticate", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = precisePattern.matcher(line);

                        if (matcher.find()) {
                            code = matcher.group(1);
                            System.out.println("🔐 Code d'authentification : " + code);
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
                        if (line.contains("AzureCloud")) {
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
                            url = null;
                            code = null;
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

    public String runScript(String scriptName) throws IOException {
        StringBuilder output = new StringBuilder();
        String scriptPath = POWERSHELL_SCRIPT_PATH + scriptName;

        sendCommand("& '" + new File(scriptPath).getAbsolutePath() + "'");

        // Lis la réponse (exemple : sur 20 lignes max ici)
        for (int i = 0; i < 20; i++) {
            String line = commandReader.readLine();
            if (line == null) break;
            output.append(line).append("\n");
        }

        return output.toString();
    }


}

