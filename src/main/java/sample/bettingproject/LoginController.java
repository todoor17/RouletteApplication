package sample.bettingproject;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {
    DBConnector database = new DBConnector();

    @FXML
    Button closeButton;
    @FXML
    AnchorPane anchorPane;
    @FXML
    public Label timeLabel;

    public void setTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLabel.setText(LocalDateTime.now().format(formatter));
            }
        };
        timer.start();
    }

    public void switchToSignUp(ActionEvent e) throws IOException {
        SceneSwitcher.switchScene(e, "signUp.fxml");
    }

    public void switchToAdmin(ActionEvent event) throws IOException {
        SceneSwitcher.switchScene(event, "admin.fxml");
    }

    Integer balance;

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public int validateLogin(String username, String password) throws SQLException {
        Connection connection = database.getConnection();

        database.selectUser(username, password);
        setBalance(database.selectBalance(username, password));

        return database.selectUser(username, password);
    }

    public boolean checkBan(String username) throws SQLException {
        boolean exists = database.checkUserExists(username);

        if (exists) {
            return database.checkBan(username);
        } else {
            return false;
        }
    }

    @FXML
    public TextField usernameF;
    @FXML
    public TextField passwordF;
    @FXML
    public Label labelWarning;
    @FXML
    public Button loginButton;

    public void connectUser(ActionEvent event) throws SQLException, IOException {
        if (usernameF.getText().isBlank()) {
            labelWarning.setText("Username field is empty");
            return;
        } else if (passwordF.getText().isBlank()) {
            labelWarning.setText("Password field is empty");
            return;
        }
        if (usernameF.getText().equals("admin") && passwordF.getText().equals("specialAdminPassword")) {
            switchToAdmin(event);
            return;
        }
        if (checkBan(usernameF.getText())) {
            labelWarning.setText("Account is banned. Contact support");
            return;
        }
        int userID = validateLogin(usernameF.getText(), passwordF.getText());
        if (userID == -1) {
            labelWarning.setText("Introduced credentials are wrong");
            return;
        }
        if (userID > 1) {
            switchToUserScene(event, userID);
        } else if (userID == 1) {
            switchToAdmin(event);
        }
    }

    private void switchToUserScene(ActionEvent event, Integer userID) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("roulette.fxml"));
        Parent root = loader.load();
        RouletteController rouletteController = loader.getController();
        rouletteController.setUserID(userID);
        rouletteController.setBalanceText(userID);
        rouletteController.setWelcomeText(event);
        rouletteController.showResultsOnPageSwitch(event);
        rouletteController.setTime();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}