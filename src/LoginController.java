package src;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.Properties;
import java.io.FileInputStream;

public class LoginController {
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    private Connection connect() throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("db.properties"));
        String url = props.getProperty("url");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    protected void handleLogin(ActionEvent event) {
        String login = loginField.getText();
        String motDePasse = passwordField.getText();
        try (Connection conn = connect()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM utilisateur WHERE login = ? AND mot_de_passe = ?");
            stmt.setString(1, login);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Charger la fenêtre principale (menu)
                try {
                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    javafx.scene.Parent menuRoot = loader.load();
                    javafx.stage.Stage stage = (javafx.stage.Stage) loginField.getScene().getWindow();
                    stage.setScene(new javafx.scene.Scene(menuRoot, 400, 350));
                    stage.setTitle("Menu Principal - Gestion de Factures");
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Erreur lors du chargement du menu principal : " + ex.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Identifiants incorrects.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Erreur de connexion à la base de données : " + e.getMessage());
            alert.showAndWait();
        }
    }
} 