package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.Year;
import java.util.List;

public class FactureFormController {
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<String> moisCombo;
    @FXML private ComboBox<Integer> anneeCombo;

    @FXML
    public void initialize() {
        // Remplir la liste des clients
        try {
            List<Client> clients = new ClientDAO().getAllClients();
            clientCombo.setItems(FXCollections.observableArrayList(clients));
        } catch (Exception e) {
            showError("Erreur chargement clients : " + e.getMessage());
        }
        // Remplir les mois
        moisCombo.setItems(FXCollections.observableArrayList(
            "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"
        ));
        // Remplir les années (5 dernières)
        ObservableList<Integer> annees = FXCollections.observableArrayList();
        int thisYear = Year.now().getValue();
        for (int i = 0; i < 5; i++) annees.add(thisYear - i);
        anneeCombo.setItems(annees);
    }

    @FXML
    private void handleGenererPDF(ActionEvent event) {
        try {
            Client client = clientCombo.getValue();
            String mois = moisCombo.getValue();
            Integer annee = anneeCombo.getValue();
            if (client == null || mois == null || annee == null) {
                showError("Veuillez sélectionner un client, un mois et une année.");
                return;
            }
            // Récupérer les prestations du client pour le mois/année
            List<Prestation> prestations = new PrestationDAO().getAllPrestations();
            List<Prestation> filtered = new java.util.ArrayList<>();
            int moisIndex = moisCombo.getItems().indexOf(mois) + 1;
            double total = 0;
            for (Prestation p : prestations) {
                if (p.getClient() != null && p.getClient().getId() == client.getId()
                    && p.getDatePrestation().getYear() == annee
                    && p.getDatePrestation().getMonthValue() == moisIndex) {
                    filtered.add(p);
                    if ("consultation".equals(p.getType())) {
                        if (p.getTjm() != null && p.getNbJours() != null) {
                            total += p.getTjm().doubleValue() * p.getNbJours();
                        }
                    } else if ("formation".equals(p.getType())) {
                        if (p.getTarifHoraire() != null && p.getHeureDebut() != null && p.getHeureFin() != null) {
                            double duree = java.time.Duration.between(p.getHeureDebut(), p.getHeureFin()).toMinutes() / 60.0;
                            total += p.getTarifHoraire().doubleValue() * duree;
                        }
                    }
                }
            }
            if (filtered.isEmpty()) {
                showInfo("Aucune prestation pour ce client et cette Periode.");
                return;
            }
            FactureGenerator.genererFacture(client, mois, annee, filtered, total);
            showInfo("Facture PDF générée avec succès !");
        } catch (Exception e) {
            showError("Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetourMenu(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("MainMenu.fxml"));
            javafx.scene.Parent menuRoot = loader.load();
            javafx.scene.Scene scene = new javafx.scene.Scene(menuRoot, 400, 350);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menu Principal - Gestion de Factures");
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        } catch (Exception ex) {
            showError("Erreur lors du retour au menu principal : " + ex.getMessage());
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
} 