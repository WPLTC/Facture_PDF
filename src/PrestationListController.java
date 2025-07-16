package src;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import javafx.event.ActionEvent;

public class PrestationListController {
    @FXML private TableView<Prestation> prestationTable;
    @FXML private TableColumn<Prestation, String> typeColumn;
    @FXML private TableColumn<Prestation, String> dateColumn;
    @FXML private TableColumn<Prestation, String> clientColumn;
    @FXML private TableColumn<Prestation, String> entrepriseColumn;
    @FXML private TableColumn<Prestation, String> descModuleColumn;
    @FXML private TableColumn<Prestation, String> tjmColumn;

    private ObservableList<Prestation> prestationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDatePrestation().toString()));
        clientColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getClient() != null ? data.getValue().getClient().getNom() : ""));
        entrepriseColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEntreprise()));
        descModuleColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            "formation".equals(data.getValue().getType()) ? data.getValue().getModule() : data.getValue().getDescription()
        ));
        tjmColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getTjm() != null ? data.getValue().getTjm().toString() : ""
        ));
        prestationTable.setItems(prestationList);
        refreshPrestations();
    }

    private void refreshPrestations() {
        try {
            List<Prestation> list = new PrestationDAO().getAllPrestations();
            prestationList.setAll(list);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du chargement des prestations : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRetourMenu(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("MainMenu.fxml"));
            javafx.scene.Parent menuRoot = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(menuRoot, 400, 350));
            stage.setTitle("Menu Principal - Gestion de Factures");
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors du retour au menu principal : " + ex.getMessage());
            alert.showAndWait();
        }
    }
} 