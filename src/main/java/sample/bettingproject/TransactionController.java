package sample.bettingproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class TransactionController {
    DBConnector database = new DBConnector();
    public ObservableList<Card> getCardsFromDatabase(String user) throws SQLException {
        Connection connection = database.getConnection();

        return database.fillCards(userID.toString());
    }

    @FXML
    ListView<Card> cardListView;

    public void initialize(ActionEvent e, String user) throws SQLException {
        ObservableList<Card> cardList = getCardsFromDatabase(userID.toString());
        cardListView.setItems(cardList);
        cardListView.setCellFactory(param -> new ListCell<Card>() {
            @Override
            protected void updateItem(Card card, boolean empty) {
                super.updateItem(card, empty);
                if (empty || card == null) {
                    setText(null);
                } else {
                    // Customize the display text as needed
                    setText("Card Number: " + card.getCardNumber() + ", CVV: " + card.getCvv() + ", Available funds: " + card.getCardAvailableFunds());
                }
            }
        });
    }

    @FXML
    Label balanceLabel;

    public void refresh(ActionEvent event) throws SQLException {
        initialize(event, userID.toString());
        balanceLabel.setText(database.selectBalanceTransaction(userID).toString());
    }

    @FXML
    TextField cardNumber;
    @FXML
    TextField cvv;
    @FXML
    TextField ammountTf;
    @FXML
    RadioButton rbAddCard;
    @FXML
    RadioButton rbDeposit;
    @FXML
    RadioButton rbWithdraw;
    @FXML
    Button makeTransaction;
    @FXML
    Label infoLabel;
    Integer userID;

    public void setUserID(Integer userID) {
        this.userID = userID;
    }
    public void addCard(String user, String cardnumber, String cvv) throws SQLException {
        database.addCard(user, cardnumber, cvv);
    }

    public void deposit(String cardNumber, String ammountString) throws SQLException {
        int ammount = parseInt(ammountString);

        Integer availableFunds = database.selectCardFunds(cardNumber);
        availableFunds = availableFunds - ammount;
        if (availableFunds < 0) {
            infoLabel.setText("Insuffiecient funds");
            infoLabel.setTextFill(Color.RED);
            ammountTf.clear();
        } else {
            database.updateFunds(availableFunds.toString(), cardNumber);

            Integer balance = database.selectBalance1(cardNumber);
            Integer userid = database.selectUserIDTransaction(cardNumber);
            balance += ammount;

            database.updateBalanceTransaction(balance.toString(), userID.toString());

            Integer cardID = database.selectCardIDTransaction(cardNumber);

            database.addTransactionDeposit(userid, cardID, ammountString);
        }
    }

    public void withdraw(String cardNumber, String ammountString) throws SQLException {
        Integer availableFunds = database.selectCardFunds(cardNumber);
        Integer balance = database.selectBalanceTransaction(cardNumber);
        Integer updatedBalance = 0;
        int ammount = parseInt(ammountString);

        availableFunds += ammount;
        if (balance < ammount) {
            infoLabel.setText("Insufficient balance");
            infoLabel.setTextFill(Color.RED);
        } else {
            database.updateFunds(availableFunds.toString(), cardNumber);

            updatedBalance = balance - ammount;

            database.updateBalanceTransaction(updatedBalance.toString(), userID.toString());

            Integer cardID = database.selectCardIDTransaction(cardNumber);

            database.addTransactionWithdraw(userID, cardID, ammountString);
        }
    }

    public boolean checkCard(String cardNumber) throws SQLException {
        return database.checkCardTransaction(cardNumber, userID);
    }
    public void backToMainPage(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("roulette.fxml"));
        Parent root = loader.load();
        RouletteController rouletteController = loader.getController();
        rouletteController.setBalanceText(userID);
        rouletteController.setUserID(userID);
        rouletteController.setTime();
        rouletteController.showResultsOnPageSwitch(event);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void changeLayout (ActionEvent event) {
        if (rbAddCard.isSelected()) {
            ammountTf.setVisible(false);
        } else if (rbDeposit.isSelected()) {
            ammountTf.setVisible(true);
        } else if (rbWithdraw.isSelected()) {
            ammountTf.setVisible(true);
        }
    }
    public void makeTransaction(ActionEvent event) throws SQLException {
        if (!rbAddCard.isSelected() && !rbDeposit.isSelected() && !rbWithdraw.isSelected()) {
            infoLabel.setText("Please select an operation");
            infoLabel.setTextFill(Color.RED);
        }
         else if (rbAddCard.isSelected()) {
            if (cardNumber.getText().isBlank() || cvv.getText().isBlank()) {
                infoLabel.setText("Some fields are empty");
                infoLabel.setTextFill(Color.RED);
            }
            else if (cardNumber.getText().length() != 16) {
                infoLabel.setText("Card number length must be 16!");
                cardNumber.clear();
                infoLabel.setTextFill(Color.RED);
            }
            else if (cvv.getText().length() != 3) {
                infoLabel.setText("CVV length must be 3!");
                cvv.clear();
                infoLabel.setTextFill(Color.RED);
            }
            else {
                if (!checkCard(cardNumber.getText())) {
                    addCard(userID.toString(), cardNumber.getText(), cvv.getText());
                    infoLabel.setText("Card was succesfully added");
                    infoLabel.setTextFill(Color.GREEN);
                } else {
                    infoLabel.setText("Card already exists. Check your cards");
                    infoLabel.setTextFill(Color.RED);
                }
            }
            refresh(event);
        } else if (rbDeposit.isSelected()) {
            int availableFunds = database.selectCardFundsDeposit(cardNumber.getText(), userID);
            int requestedFunds = parseInt(ammountTf.getText());

            if (cardNumber.getText().isBlank() || cvv.getText().isBlank() || ammountTf.getText().isBlank()) {
                infoLabel.setText("Some fields are empty");
                infoLabel.setTextFill(Color.RED);
            } else if (cardNumber.getText().length() != 16) {
                infoLabel.setText("Card number length must be 16!");
                cardNumber.clear();
                infoLabel.setTextFill(Color.RED);
                cardNumber.clear();
            } else if (cvv.getText().length() != 3) {
                infoLabel.setText("CVV length must be 3!");
                cvv.clear();
                infoLabel.setTextFill(Color.RED);
                cvv.clear();
            } else if (ammountTf.getText().equals("")) {
                infoLabel.setText("Ammount field is empty");
                infoLabel.setTextFill(Color.RED);
            } else {
                if (checkCard(cardNumber.getText())) {
                    if (availableFunds < requestedFunds) {
                        infoLabel.setText("Card doesnt have enough money!");
                        infoLabel.setTextFill(Color.RED);
                    } else {
                        deposit(cardNumber.getText(), ammountTf.getText());
                        infoLabel.setText("Succesful deposit");
                        infoLabel.setTextFill(Color.GREEN);
                        refresh(event);
                    }
                } else {
                    infoLabel.setText("This card is not in our database");
                    infoLabel.setTextFill(Color.RED);
                }
            }
        } else if (rbWithdraw.isSelected()) {
            Integer balance = database.selectBalanceWithdraw(userID);
            int requestedSum = parseInt(ammountTf.getText());

            if (cardNumber.getText().isBlank() || cvv.getText().isBlank() || ammountTf.getText().isBlank()) {
                infoLabel.setText("Some fields are empty");
                infoLabel.setTextFill(Color.RED);
            } else if (cardNumber.getText().length() != 16) {
                infoLabel.setText("Card number length must be 16!");
                cardNumber.clear();
                infoLabel.setTextFill(Color.RED);
                cardNumber.clear();
            } else if (cvv.getText().length() != 3) {
                infoLabel.setText("CVV length must be 3!");
                cvv.clear();
                infoLabel.setTextFill(Color.RED);
                cvv.clear();
            } else if (ammountTf.getText().equals("")) {
                infoLabel.setText("Ammount field is empty");
                infoLabel.setTextFill(Color.RED);
            } else {
                if (checkCard(cardNumber.getText())) {
                    if (requestedSum > balance) {
                        infoLabel.setText("Requested sum is bigger than account balance");
                        infoLabel.setTextFill(Color.RED);
                    } else {
                        withdraw(cardNumber.getText(), ammountTf.getText());
                        infoLabel.setText("Succesful withdraw");
                        infoLabel.setTextFill(Color.GREEN);
                        refresh(event);
                    }
                } else {
                    infoLabel.setText("This card is not in our database");
                    infoLabel.setTextFill(Color.RED);
                }
            }
        }
    }
}
