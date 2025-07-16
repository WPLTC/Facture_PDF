package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainMenuController {
    @FXML
    private void handleSaisirPrestation(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("PrestationForm.fxml"));
            javafx.scene.Parent prestationRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(prestationRoot, 500, 500));
            stage.setTitle("Saisie de Prestation");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture du formulaire de prestation : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGererClients(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("ClientManager.fxml"));
            javafx.scene.Parent clientRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(clientRoot, 500, 400));
            stage.setTitle("Gestion des clients");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture de la gestion des clients : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGenererFacture(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("FactureForm.fxml"));
            javafx.scene.Parent factureRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(factureRoot, 400, 350));
            stage.setTitle("Generer une facture");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture de la génération de facture : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleGenererBilan(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("BilanForm.fxml"));
            javafx.scene.Parent bilanRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(bilanRoot, 450, 350));
            stage.setTitle("Générer un bilan");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture de la génération de bilan : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeconnexion(ActionEvent event) {
        // TODO: revenir à la page de login
        showInfo("Déconnexion");
    }

    @FXML
    private void handleAfficherPrestations(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("PrestationList.fxml"));
            javafx.scene.Parent prestationRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(prestationRoot, 800, 500));
            stage.setTitle("Liste des prestations");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'ouverture de la liste des prestations : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 