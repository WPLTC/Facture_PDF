package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;

public class BilanFormController {
    @FXML private ComboBox<String> periodeCombo;
    @FXML private ComboBox<String> moisCombo;
    @FXML private ComboBox<Integer> anneeCombo;
    @FXML private DatePicker debutPicker;
    @FXML private DatePicker finPicker;

    @FXML
    public void initialize() {
        periodeCombo.setItems(FXCollections.observableArrayList("Mensuel", "Annuel", "Periode personnalisée"));
        periodeCombo.setValue("Mensuel");
        moisCombo.setItems(FXCollections.observableArrayList(
            "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"
        ));
        moisCombo.setValue("Janvier");
        ObservableList<Integer> annees = FXCollections.observableArrayList();
        int thisYear = Year.now().getValue();
        for (int i = 0; i < 5; i++) annees.add(thisYear - i);
        anneeCombo.setItems(annees);
        anneeCombo.setValue(thisYear);
        updateFields();
        periodeCombo.setOnAction(e -> updateFields());
    }

    private void updateFields() {
        String periode = periodeCombo.getValue();
        boolean isMensuel = "Mensuel".equals(periode);
        boolean isAnnuel = "Annuel".equals(periode);
        boolean isPeriode = "Periode personnalisée".equals(periode);
        moisCombo.setDisable(!isMensuel);
        debutPicker.setDisable(!isPeriode);
        finPicker.setDisable(!isPeriode);
        anneeCombo.setDisable(!isMensuel && !isAnnuel);
        // Correction du titre de la fenêtre sans accent
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) periodeCombo.getScene().getWindow();
            stage.setTitle("Generer un bilan");
        } catch (Exception e) {}
    }

    @FXML
    private void handleGenererPDF(ActionEvent event) {
        try {
            String periode = periodeCombo.getValue();
            LocalDate debut, fin;
            if ("Mensuel".equals(periode)) {
                int mois = moisCombo.getSelectionModel().getSelectedIndex() + 1;
                int annee = anneeCombo.getValue();
                debut = LocalDate.of(annee, mois, 1);
                fin = debut.withDayOfMonth(debut.lengthOfMonth());
            } else if ("Annuel".equals(periode)) {
                int annee = anneeCombo.getValue();
                debut = LocalDate.of(annee, 1, 1);
                fin = LocalDate.of(annee, 12, 31);
            } else {
                debut = debutPicker.getValue();
                fin = finPicker.getValue();
                if (debut == null || fin == null || debut.isAfter(fin)) {
                    showError("Veuillez sélectionner une Periode valide.");
                    return;
                }
            }
            List<Prestation> prestations = new PrestationDAO().getAllPrestations();
            BilanGenerator.genererBilan(prestations, debut, fin);
            showInfo("Bilan PDF généré avec succès !");
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