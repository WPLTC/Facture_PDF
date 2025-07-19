package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientManagerController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nomColumn;
    @FXML private TableColumn<Client, String> entrepriseColumn;
    @FXML private TextField nomField;
    @FXML private TextField entrepriseField;

    private ObservableList<Client> clientList = FXCollections.observableArrayList();
    private Client selectedClient = null;

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        entrepriseColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEntreprise()));
        clientTable.setItems(clientList);
        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selectedClient = newSel;
            if (newSel != null) {
                nomField.setText(newSel.getNom());
                entrepriseField.setText(newSel.getEntreprise());
            }
        });
        refreshClients();
    }

    private void refreshClients() {
        try {
            clientList.setAll(new ClientDAO().getAllClients());
        } catch (Exception e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouter() {
        try {
            new ClientDAO().ajouterClient(nomField.getText(), entrepriseField.getText());
            refreshClients();
            nomField.clear();
            entrepriseField.clear();
        } catch (Exception e) {
            showError("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifier() {
        if (selectedClient == null) return;
        try {
            new ClientDAO().modifierClient(selectedClient.getId(), nomField.getText(), entrepriseField.getText());
            refreshClients();
        } catch (Exception e) {
            showError("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimer() {
        if (selectedClient == null) return;
        try {
            new ClientDAO().supprimerClient(selectedClient.getId());
            refreshClients();
            nomField.clear();
            entrepriseField.clear();
        } catch (Exception e) {
            showError("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetourMenu(javafx.event.ActionEvent event) {
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du retour au menu principal : " + ex.getMessage());
            alert.showAndWait();
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
} 