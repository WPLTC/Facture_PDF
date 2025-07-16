package src;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.text.Normalizer;

public class FactureGenerator {
    // Méthode utilitaire pour supprimer les accents
    private static String removeAccents(String input) {
        if (input == null) return null;
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^a-zA-Z0-9_ -]", "_");
    }

    public static void genererFacture(Client client, String mois, int annee, List<Prestation> prestations, double total) throws IOException {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        // Charger la police TTF (assure-toi que le fichier existe dans lib/)
        PDType0Font font = PDType0Font.load(doc, new File("lib/LiberationSans-Regular.ttf"));

        PDPageContentStream content = new PDPageContentStream(doc, page);
        content.setFont(font, 18);
        content.beginText();
        content.newLineAtOffset(50, 770);
        content.showText("Facture - " + removeAccents(mois) + " " + annee);
        content.endText();

        content.setFont(font, 12);
        content.beginText();
        content.newLineAtOffset(50, 740);
        content.showText("Client : " + removeAccents(client.getNom()) + (client.getEntreprise() != null && !client.getEntreprise().isEmpty() ? " (" + removeAccents(client.getEntreprise()) + ")" : ""));
        content.endText();

        int y = 700;
        for (Prestation p : prestations) {
            content.beginText();
            content.newLineAtOffset(50, y);
            String detail = removeAccents(p.getType()) + " - " + p.getDatePrestation() + " - " + (p.getModule() != null ? removeAccents(p.getModule()) : removeAccents(p.getDescription()));
            if ("consultation".equals(p.getType())) {
                detail += " - TJM: " + (p.getTjm() != null ? p.getTjm() : "") + " x Nb jours: " + (p.getNbJours() != null ? p.getNbJours() : "");
                if (p.getTjm() != null && p.getNbJours() != null) {
                    detail += " = " + p.getTjm().multiply(new java.math.BigDecimal(p.getNbJours())).setScale(2, java.math.RoundingMode.HALF_UP) + " EUR";
                }
            } else if ("formation".equals(p.getType())) {
                detail += " - Tarif horaire: " + (p.getTarifHoraire() != null ? p.getTarifHoraire() : "");
                if (p.getHeureDebut() != null && p.getHeureFin() != null && p.getTarifHoraire() != null) {
                    double duree = java.time.Duration.between(p.getHeureDebut(), p.getHeureFin()).toMinutes() / 60.0;
                    java.math.BigDecimal montant = p.getTarifHoraire().multiply(new java.math.BigDecimal(duree)).setScale(2, java.math.RoundingMode.HALF_UP);
                    detail += " x Duree: " + String.format("%.2f", duree) + "h = " + montant + " EUR";
                }
            }
            content.showText(detail);
            content.endText();
            y -= 20;
        }

        content.setFont(font, 14);
        content.beginText();
        content.newLineAtOffset(50, y - 20);
        content.showText("Total : " + total + " EUR");
        content.endText();

        content.close();
        // Nettoyer les accents dans le nom du fichier
        String nomClient = removeAccents(client.getNom());
        String moisSansAccent = removeAccents(mois);
        String fileName = String.format("facture_%s_%s_%d.pdf", nomClient, moisSansAccent, annee);
        doc.save(new File(fileName));
        doc.close();
    }
} 