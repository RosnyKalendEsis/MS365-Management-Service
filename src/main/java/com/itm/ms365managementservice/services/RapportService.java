package com.itm.ms365managementservice.services;

import com.itm.ms365managementservice.entities.Rapport;
import com.itm.ms365managementservice.repositories.RapportRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RapportService {

    private final RapportRepository rapportRepository;

    public List<Rapport> getAllRapports() {
        return rapportRepository.findAll();
    }

    public Optional<Rapport> getRapportById(String id) {
        return rapportRepository.findById(id);
    }

    public Rapport createRapport(Rapport rapport) {
        return rapportRepository.save(rapport);
    }

    public Rapport updateRapport(String id, Rapport updatedRapport) {
        if (rapportRepository.existsById(id)) {
            updatedRapport.setId(id);
            return rapportRepository.save(updatedRapport);
        }
        throw new RuntimeException("Rapport not found with id: " + id);
    }

    public void deleteRapport(String id) {
        rapportRepository.deleteById(id);
    }
}

