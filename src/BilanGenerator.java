package src;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;
import java.text.Normalizer;

public class BilanGenerator {
    private static String removeAccents(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9_ -]", "_");
    }

    public static void genererBilan(List<Prestation> prestations, LocalDate debut, LocalDate fin) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);
        PDType0Font font = PDType0Font.load(doc, new File("lib/LiberationSans-Regular.ttf"));
        PDPageContentStream content = new PDPageContentStream(doc, page);
        content.setFont(font, 18);
        content.beginText();
        content.newLineAtOffset(50, 770);
        content.showText("Bilan chiffre d'affaires");
        content.endText();
        content.setFont(font, 12);
        content.beginText();
        content.newLineAtOffset(50, 740);
        content.showText("Periode : " + debut + " au " + fin);
        content.endText();
        int y = 700;
        Map<String, BigDecimal> totalParClient = new LinkedHashMap<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;
        for (Prestation p : prestations) {
            if (p.getDatePrestation().isBefore(debut) || p.getDatePrestation().isAfter(fin)) continue;
            String client = p.getClient() != null ? removeAccents(p.getClient().getNom()) : "(inconnu)";
            BigDecimal montant = BigDecimal.ZERO;
            if ("consultation".equals(p.getType())) {
                if (p.getTjm() != null && p.getNbJours() != null)
                    montant = p.getTjm().multiply(new BigDecimal(p.getNbJours()));
            } else if ("formation".equals(p.getType())) {
                if (p.getTarifHoraire() != null && p.getHeureDebut() != null && p.getHeureFin() != null) {
                    double duree = java.time.Duration.between(p.getHeureDebut(), p.getHeureFin()).toMinutes() / 60.0;
                    montant = p.getTarifHoraire().multiply(new BigDecimal(duree));
                }
            }
            totalParClient.put(client, totalParClient.getOrDefault(client, BigDecimal.ZERO).add(montant));
            totalGeneral = totalGeneral.add(montant);
            content.beginText();
            content.newLineAtOffset(50, y);
            content.showText(client + " - " + p.getType() + " - " + p.getDatePrestation() + " : " + montant.setScale(2, BigDecimal.ROUND_HALF_UP) + " EUR");
            content.endText();
            y -= 18;
            if (y < 100) {
                content.close();
                page = new PDPage(PDRectangle.A4);
                doc.addPage(page);
                content = new PDPageContentStream(doc, page);
                content.setFont(font, 12);
                y = 750;
            }
        }
        y -= 10;
        content.setFont(font, 14);
        content.beginText();
        content.newLineAtOffset(50, y);
        content.showText("Totaux par client :");
        content.endText();
        y -= 20;
        content.setFont(font, 12);
        for (Map.Entry<String, BigDecimal> entry : totalParClient.entrySet()) {
            content.beginText();
            content.newLineAtOffset(60, y);
            content.showText(entry.getKey() + " : " + entry.getValue().setScale(2, BigDecimal.ROUND_HALF_UP) + " EUR");
            content.endText();
            y -= 16;
        }
        y -= 10;
        content.setFont(font, 16);
        content.beginText();
        content.newLineAtOffset(50, y);
        content.showText("Total general : " + totalGeneral.setScale(2, BigDecimal.ROUND_HALF_UP) + " EUR");
        content.endText();
        content.close();
        String fileName = String.format("bilan_%s_au_%s", debut, fin).replaceAll("[^a-zA-Z0-9_ -]", "_") + ".pdf";
        doc.save(new File(fileName));
        doc.close();
    }
} 