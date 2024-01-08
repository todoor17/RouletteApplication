package sample.bettingproject;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;

import static java.lang.Math.abs;

public class AdminController {
    DBConnector database = new DBConnector();

    @FXML
    private TextField textField;
    @FXML
    private ListView<UserData> listView;
    @FXML
    private ListView<Transactions> listView1;
    @FXML
    Label label;

    public ObservableList<UserData> getUserData() throws SQLException {
        return database.fillUserDataList();
    }

    public ObservableList<Transactions> getTransactionsHistroy(String username) throws SQLException {
        return database.fillTransaction(username);
    }

    public void getUsers(ActionEvent event) throws SQLException {
        listView.setVisible(true);
        listView1.setVisible(false);
        ObservableList<UserData> userdataList = getUserData();
        listView.setItems(userdataList);
        listView.setCellFactory(param -> new ListCell<UserData>() {
            @Override
            protected void updateItem(UserData userdata, boolean empty) {
                super.updateItem(userdata, empty);
                if (empty || userdata == null) {
                    setText(null);
                } else {
                    setText("User ID: " + userdata.getUserid() + "    " +
                            "Username: " + userdata.getUsername() + "    " +
                            "Balance: " + userdata.getBalance() + "    " +
                            "Ban Status: " + userdata.getBanstatus() + "    " +
                            "Email: " + userdata.getEmail() + "    " +
                            "First Name: " + userdata.getFirstname() + "    " +
                            "Last Name: " + userdata.getLastname() + "    " +
                            "CNP: " + userdata.getCnp() + "    " +
                            "Address: " + userdata.getAddress() + "    " +
                            "Password" + userdata.getPassword());
                }
            }
        });
    }

    public void getTransactionHistory(ActionEvent event) throws SQLException {
        listView1.setVisible(true);
        listView.setVisible(false);
        ObservableList<Transactions> transactionsList = getTransactionsHistroy(textField.getText());
        listView1.setItems(transactionsList);
        listView1.setCellFactory(param -> new ListCell<Transactions>() {
            @Override
            protected void updateItem(Transactions transaction, boolean empty) {
                super.updateItem(transaction, empty);
                if (empty || transaction == null) {
                    setText(null);
                } else {
                    // Customize the display text as needed
                    setText("Transaction ID: " + transaction.getTransactionid() + "    " +
                            "User ID: " + transaction.getUserid() + "    " +
                            "Card ID: " + transaction.getCardid() + "    " +
                            "Type of transaction: " + transaction.getTypeoftransaction() + "    " +
                            "Value: " + transaction.getValue() + "    " +
                            "Date: " + transaction.getDataoftransaction());
                }
            }
        });
    }

    public void banUser(ActionEvent event) throws SQLException {
        listView1.setVisible(false);
        listView.setVisible(true);

        database.banUser(textField.getText());
        getUsers(event);
    }

    public void unbanUser(ActionEvent event) throws SQLException {
        listView1.setVisible(false);
        listView.setVisible(true);

        database.UnbanUser(textField.getText());
        getUsers(event);
    }

    public void showSummary(ActionEvent event) throws SQLException {
        String username = textField.getText();
        Integer nrOfBets = database.selectNrOfBetsAdmin(username);
        Integer sumBet = database.selectSumBetAdmin(username);
        Integer sumWon = database.selectSumWonAdmin(username);
        Integer sumLost = database.selectSumLostAdmin(username);

        if (sumWon > abs(sumLost)) {
            label.setText("User placed " + nrOfBets + " bet(s). He bet " + sumBet + " and he won " + (sumWon - abs(sumLost)));
            label.setTextFill(Color.RED);
        } else if (sumWon < abs(sumLost)) {
            label.setText("User placed " + nrOfBets + " bet(s). He bet " + sumBet + " and he lost " + (abs(sumLost) - sumWon));
            label.setTextFill(Color.GREEN);
        } else if (sumWon == abs(sumLost)) {
            label.setText("User placed " + nrOfBets + " bet(s). His winnings and losses are equal.");
            label.setTextFill(Color.WHITE);
        }
    }

    public void backToLogin(ActionEvent event) throws IOException {
        SceneSwitcher.switchScene(event, "login.fxml");
    }
}