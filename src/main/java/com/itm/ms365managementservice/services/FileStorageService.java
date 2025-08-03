package com.itm.ms365managementservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final String uploadDir = "./uploads";

    public String save(MultipartFile file, String directory) {
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide.");
        }

        try {
            Path targetDir = (directory == null || directory.trim().isEmpty())
                    ? Paths.get(uploadDir)
                    : Paths.get(uploadDir, directory);
            Files.createDirectories(targetDir);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = targetDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return targetDir.getFileName() + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Erreur de sauvegarde du fichier : " + e.getMessage());
        }
    }
}


