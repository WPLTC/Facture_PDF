package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class PrestationFormController {
    @FXML private ComboBox<String> typeCombo;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureDebutField;
    @FXML private TextField heureFinField;
    @FXML private TextField classeField;
    @FXML private TextField moduleField;
    @FXML private TextField descriptionField;
    @FXML private TextField tjmField;
    @FXML private TextField entrepriseField;
    @FXML private TextField clientField;
    @FXML private TextField tarifHoraireField;
    @FXML private TextField nbJoursField;
    @FXML private Label labelHeureDebut;
    @FXML private Label labelHeureFin;
    @FXML private Label labelClasse;
    @FXML private Label labelModule;
    @FXML private Label labelDescription;
    @FXML private Label labelTJM;
    @FXML private Label labelTarifHoraire;
    @FXML private Label labelNbJours;
    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<String> entrepriseCombo;

    @FXML
    public void initialize() {
        typeCombo.getItems().addAll("formation", "consultation");
        typeCombo.setOnAction(e -> updateFields());
        updateFields();

        // Ajout du TextFormatter pour heureDebutField
        heureDebutField.setPromptText("hh:mm");
        heureDebutField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            // Si on tape 2 chiffres sans :, on insère automatiquement le :
            if (newText.length() == 2 && !newText.contains(":") && change.isAdded()) {
                change.setText(change.getText() + ":");
                return change;
            }
            // Autorise uniquement le format hh:mm ou moins
            if (newText.matches("^\\d{0,2}:?\\d{0,2}$")) {
                return change;
            }
            return null;
        }));

        // Ajout du TextFormatter pour heureFinField
        heureFinField.setPromptText("hh:mm");
        heureFinField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() == 2 && !newText.contains(":") && change.isAdded()) {
                change.setText(change.getText() + ":");
                return change;
            }
            if (newText.matches("^\\d{0,2}:?\\d{0,2}$")) {
                return change;
            }
            return null;
        }));

        // Remplissage des ComboBox client et entreprise
        try {
            java.util.List<Client> clients = new ClientDAO().getAllClients();
            clientCombo.setItems(javafx.collections.FXCollections.observableArrayList(clients));
            java.util.Set<String> entreprises = clients.stream()
                .map(Client::getEntreprise)
                .filter(e -> e != null && !e.isEmpty())
                .collect(java.util.stream.Collectors.toCollection(java.util.TreeSet::new));
            entrepriseCombo.setItems(javafx.collections.FXCollections.observableArrayList(entreprises));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement des clients/entreprises : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void updateFields() {
        String type = typeCombo.getValue();
        boolean isFormation = "formation".equals(type);
        // Afficher/masquer les champs selon le type
        heureDebutField.setVisible(isFormation);
        heureFinField.setVisible(isFormation);
        classeField.setVisible(isFormation);
        moduleField.setVisible(isFormation);
        labelHeureDebut.setVisible(isFormation);
        labelHeureFin.setVisible(isFormation);
        labelClasse.setVisible(isFormation);
        labelModule.setVisible(isFormation);
        tarifHoraireField.setVisible(isFormation);
        labelTarifHoraire.setVisible(isFormation);

        descriptionField.setVisible(!isFormation);
        tjmField.setVisible(!isFormation);
        nbJoursField.setVisible(!isFormation);
        labelDescription.setVisible(!isFormation);
        labelTJM.setVisible(!isFormation);
        labelNbJours.setVisible(!isFormation);
    }

    @FXML
    private void handleEnregistrer(ActionEvent event) {
        try {
            String type = typeCombo.getValue();
            java.time.LocalDate date = datePicker.getValue();
            java.time.LocalTime heureDebut = heureDebutField.isVisible() && !heureDebutField.getText().isEmpty() ? java.time.LocalTime.parse(heureDebutField.getText()) : null;
            java.time.LocalTime heureFin = heureFinField.isVisible() && !heureFinField.getText().isEmpty() ? java.time.LocalTime.parse(heureFinField.getText()) : null;
            String classe = classeField.isVisible() ? classeField.getText() : null;
            String module = moduleField.isVisible() ? moduleField.getText() : null;
            String description = descriptionField.isVisible() ? descriptionField.getText() : null;
            java.math.BigDecimal tjm = tjmField.isVisible() && !tjmField.getText().isEmpty() ? new java.math.BigDecimal(tjmField.getText()) : null;
            java.math.BigDecimal tarifHoraire = tarifHoraireField.isVisible() && !tarifHoraireField.getText().isEmpty() ? new java.math.BigDecimal(tarifHoraireField.getText()) : null;
            Integer nbJours = nbJoursField.isVisible() && !nbJoursField.getText().isEmpty() ? Integer.parseInt(nbJoursField.getText()) : null;
            String entreprise = entrepriseCombo.getValue();
            Client client = clientCombo.getValue();
            if (client == null || entreprise == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un client et une entreprise.");
                alert.showAndWait();
                return;
            }
            PrestationDAO dao = new PrestationDAO();
            dao.ajouterPrestation(type, date, heureDebut, heureFin, classe, module, description, tjm, entreprise, client.getNom(), tarifHoraire, nbJours);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Prestation enregistrée avec succès !");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de l'enregistrement : " + e.getMessage());
            alert.showAndWait();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du retour au menu principal : " + ex.getMessage());
            alert.showAndWait();
        }
    }
} 