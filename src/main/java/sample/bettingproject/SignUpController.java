package sample.bettingproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SignUpController {
    DBConnector database = new DBConnector();
    public void switchToScene1(ActionEvent event) throws IOException {
        SceneSwitcher.switchScene(event, "login.fxml");
    }

    @FXML
    Button signupButton;
    @FXML
    TextField usernameF;
    @FXML
    TextField emailF;
    @FXML
    TextField passwordF;
    @FXML
    TextField passwordF1;
    @FXML
    TextField firstnameF;
    @FXML
    TextField lastnameF;
    @FXML
    TextField cnpF;
    @FXML
    TextField addressF;
    @FXML
    RadioButton rbYes;
    @FXML
    RadioButton rbNo;
    @FXML
    Label labelWarning;
    @FXML
    Label labelWarning1;

    public void insertInDatabase(String username, String email, String password, String firstname, String lastname, String cnp, String address, String balance) throws SQLException {
        database.insertData(username, email, password, firstname, lastname, cnp, address, balance);
    }

    public boolean check(Integer usernameOrEmailOrPhoneOrCNP, String string) throws SQLException {
        return database.checkInfo(usernameOrEmailOrPhoneOrCNP, string);
    }

    public void signUpButton(ActionEvent event) throws SQLException {
        if (usernameF.getText().isBlank() || emailF.getText().isBlank() || passwordF.getText().isBlank() || firstnameF.getText().isBlank()
                || lastnameF.getText().isBlank() || cnpF.getText().isBlank() || addressF.getText().isBlank()) {
            labelWarning.setText("Please fill all fields available");
            labelWarning1.setText("");
        } else if (!check(0, usernameF.getText())) {
            labelWarning.setText("Username is already taken");
            labelWarning1.setText("");
            usernameF.clear();
        } else if (!check(1, emailF.getText())) {
            labelWarning.setText("Email is already taken");
            labelWarning1.setText("");
            emailF.clear();
        } else if (!check(2, cnpF.getText())) {
            labelWarning.setText("CNP is already taken");
            labelWarning1.setText("");
            cnpF.clear();
        } else if (!passwordF.getText().equals(passwordF1.getText())) {
            labelWarning.setText("Passwords dont match");
            labelWarning1.setText("");
            passwordF1.clear();
        } else if (usernameF.getText().equals(passwordF.getText())) {
            labelWarning.setText("Username and password must be different");
            labelWarning1.setText("");
            passwordF.clear();
            passwordF1.clear();
        } else if (passwordF.getText().length() < 8 || passwordF1.getText().length() < 8) {
            labelWarning.setText("Password must be at least 8 characters long");
            labelWarning1.setText("");
            passwordF1.clear();
            passwordF.clear();
        } else if (cnpF.getText().length() != 13) {
            labelWarning.setText("CNP length must be 13");
            labelWarning1.setText("");
            cnpF.clear();
        } else {
            labelWarning1.setText("Your registration was completed");
            labelWarning.setText("");
            if (rbYes.isSelected())
                insertInDatabase(usernameF.getText(), emailF.getText(), passwordF.getText(), firstnameF.getText(), lastnameF.getText(), cnpF.getText(), addressF.getText(), "100");
            else
                insertInDatabase(usernameF.getText(), emailF.getText(), passwordF.getText(), firstnameF.getText(), lastnameF.getText(), cnpF.getText(), addressF.getText(), "0");

        }
        if (rbYes.isSelected()) {

        }
    }
}
