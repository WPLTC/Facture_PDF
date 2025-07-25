package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(root, 400, 250);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Connexion - Gestion de Factures");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true); // Ouvre en plein écran
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 