package sample.bettingproject;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class StartController {
    @FXML
    private Node rootNode;

    @FXML
    public void initialize() {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            try {
                switchToLogin();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        delay.play();
    }

    private void switchToLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setTime();
        Stage stage = (Stage) rootNode.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login Scene");
    }
}
