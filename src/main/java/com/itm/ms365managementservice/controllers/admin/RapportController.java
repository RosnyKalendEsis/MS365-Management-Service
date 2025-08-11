package com.itm.ms365managementservice.controllers.admin;

import com.itm.ms365managementservice.entities.Rapport;
import com.itm.ms365managementservice.services.RapportService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rapports")
public class RapportController {

    private final RapportService rapportService;

    @GetMapping
    public List<Rapport> getAllRapports() {
        return rapportService.getAllRapports();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> exportStatsToPdf() {

        String fileName = "statistiques-" + LocalDate.now() + ".pdf";
        String folder = "reports";
        File directory = new File(folder);
        if (!directory.exists()) directory.mkdirs();

        String path = folder + "/" + fileName;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("📝 Rapport Annuel 2023", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("🕒 Généré le : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            document.add(new Paragraph(" "));

            Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD);
            document.add(new Paragraph("📌 Contenu :", sectionFont));
            document.add(new Paragraph(" "));

            Font textFont = new Font(Font.HELVETICA, 12);
            document.add(new Paragraph("Ceci est le contenu temporaire du rapport. Les données réelles seront ajoutées plus tard selon les exigences métier.", textFont));

            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph("Généré automatiquement par le système - MS365 Management", new Font(Font.HELVETICA, 9, Font.ITALIC)));

            document.close();

            byte[] pdfBytes = Files.readAllBytes(Path.of(path));

            Rapport rapport = new Rapport();
            rapport.setTitle("Rapport Annuel 2023");
            rapport.setReference("RA-2023-045");
            rapport.setType("rapport");
            rapport.setDate(LocalDate.now().toString());
            rapport.setStatus("valide");
            rapport.setAuthor("Admin IT");
            rapport.setSize(String.format("%.1f Mo", pdfBytes.length / 1024f / 1024f));
            rapport.setFormat("pdf");
            rapport.setDescription("Rapport annuel des activités parlementaires");
            rapport.setUrl("/uploads/" + path);

            rapportService.createRapport(rapport);

            return ResponseEntity.ok(path);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la génération du PDF");
        }
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

