package sample.bettingproject;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

public class RouletteController {
    @FXML
    Label resultRed;
    @FXML
    Label resultBlack;
    @FXML
    Label resultGreen;
    @FXML
    Label resultRed1;
    @FXML
    Label resultBlack1;
    @FXML
    Label resultGreen1;
    @FXML
    Label resultRed2;
    @FXML
    Label resultBlack2;
    @FXML
    Label resultGreen2;
    @FXML
    Label resultRed3;
    @FXML
    Label resultBlack3;
    @FXML
    Label resultGreen3;
    @FXML
    Label resultRed4;
    @FXML
    Label resultBlack4;
    @FXML
    Label resultGreen4;
    @FXML
    Label label5 = new Label();
    @FXML
    Rectangle rectangleResult = new Rectangle(45, 45);

    private boolean betPlaced = false;
    Integer userID;

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    Integer sessionIDBet;

    public void setSessionIDBet(Integer sessionID) {
        this.sessionIDBet = sessionID;
    }

    Integer sessionIDResult;

    public void setSessionIDResult(Integer sessionID) {
        this.sessionIDResult = sessionID;
    }

    public void getResultI(Integer[] redNumbers, Integer[] blackNumbers, Integer result, Label labelRed, Label labelBlack, Label labelGreen) {
        for (int i = 0; i < redNumbers.length; i++) {
            if (result == redNumbers[i]) {
                labelRed.setText(result.toString());
                labelBlack.setText("");
                labelGreen.setText("");
            }
        }
        for (int i = 0; i < blackNumbers.length; i++) {
            if (result == blackNumbers[i]) {
                labelRed.setText("");
                labelBlack.setText(result.toString());
                labelGreen.setText("");
            }
        }
        if (result == 0) {
            resultRed.setText("");
            resultBlack.setText("");
            resultGreen.setText(result.toString());
        }
    }

    public void showResultsOnPageSwitch(ActionEvent event) throws SQLException {
        Integer[] redNumbers = new Integer[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        Integer[] blackNumbers = new Integer[]{2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        Integer[] results = new Integer[5];
        database.updateResultsArrayOnLogin(results);
        //results[0] = -4, results[1] = -3, results[2] = -2, results[3] = -1;
        getResultI(redNumbers, blackNumbers, results[0], resultRed4, resultBlack4, resultGreen4);
        getResultI(redNumbers, blackNumbers, results[1], resultRed3, resultBlack3, resultGreen3);
        getResultI(redNumbers, blackNumbers, results[2], resultRed2, resultBlack2, resultGreen2);
        getResultI(redNumbers, blackNumbers, results[3], resultRed1, resultBlack1, resultGreen1);
        getResultI(redNumbers, blackNumbers, results[4], resultRed, resultBlack, resultGreen);
    }

    public void showLast5Results(Integer result, ActionEvent event) throws SQLException {
        Integer[] redNumbers = new Integer[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        Integer[] blackNumbers = new Integer[]{2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        Integer[] results = new Integer[4];
        int index = 0;
        database.updateResultsArrayOnGetResult(results);
        //results[0] = -4, results[1] = -3, results[2] = -2, results[3] = -1;
        if (results[3] == -1) {
            database.specialUpdateResults(results);
        }


        getResultI(redNumbers, blackNumbers, results[0], resultRed4, resultBlack4, resultGreen4);
        getResultI(redNumbers, blackNumbers, results[1], resultRed3, resultBlack3, resultGreen3);
        getResultI(redNumbers, blackNumbers, results[2], resultRed2, resultBlack2, resultGreen2);
        getResultI(redNumbers, blackNumbers, results[3], resultRed1, resultBlack1, resultGreen1);
        getResultI(redNumbers, blackNumbers, result, resultRed, resultBlack, resultGreen);
    }
    DBConnector database = new DBConnector();
    public void getResult(ActionEvent event) throws SQLException {
        Color color = Color.BLACK;
        rectangleResult.setFill(color);
        Integer red = 0, black = 0, green = 0;
        Random rand = new Random();
        Integer[] redNumbers = new Integer[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
        Integer[] blackNumbers = new Integer[]{2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
        Integer result = rand.nextInt(37);

        betInfo.setText("");


        showLast5Results(result, event);

        for (int i = 0; i < redNumbers.length; i++) {
            if (result == redNumbers[i]) {
                red = 1;
                black = 0;
                green = 0;
            }
        }
        for (int i = 0; i < blackNumbers.length; i++) {
            if (result == blackNumbers[i]) {
                red = 0;
                black = 1;
                green = 0;
            }
        }
        if (result == 0) {
            red = 0;
            black = 0;
            green = 1;
        }

        Integer sessionID = database.selectSessionId();
        setSessionIDResult(sessionID);

        Integer resultWhenMax = database.selectResultWhenMax(sessionID);
        Integer nrOfBets = database.selectNrOfBets(sessionID);
        Integer recievedSum = database.selectRecievedSum(sessionID);

        if (resultWhenMax == null) { //if sessionid != null and result == null, i have to update the database
            database.updateRouletteSessionWhenBet(result, userID, nrOfBets, recievedSum, sessionID);
        } else if (resultWhenMax != null){ //otherwise, i have to insert details of a new session, when no bet has been placed
            database.updateRouletteSessionWhenNoBet(result, userID);
        }

        database.updateStatus(sessionID);

        if (red == 1) {
            database.updateWonRed(sessionID);
        }
        else if (black == 1) {
            database.updateWonBlack(sessionID);
        }
        else if (green == 1) {
            database.updateWonGreen(sessionID);
        }
        for (Integer i = 0; i <= 36; i++) {
            if (result == i) {
                database.updateWonNumber(sessionID, i);
                break;
            }
        }
        if (result >= 1 && result <= 12) {
            database.updateWon1st12("1st 12", sessionID);
        }
        else if (result > 12 && result <= 24) {
            database.updateWon2nd12("2nd 12", sessionID);
        }
        else if (result > 24 && result <= 36) {
            database.updateWon3rd12("3rd 12", sessionID);
        }
        if (result >= 1 && result <= 18) {
            database.updateWon1_18("1-18", sessionID);
        }
        else if (result > 18 && result <= 36) {
            database.updateWon19_36("19-36", sessionID);
        }
        if (result % 2 == 0 && result != 0) {
            database.updateWonEven("Even", sessionID);
        }
        else if (result % 2 == 1 && result != 0){
            database.updateWonOdd("Odd", sessionID);
        }

        database.updateActualWin2x(sessionID);
        database.updateActualWin3x(sessionID);
        database.updateActualWin36x(sessionID);
        database.updateActualWinLost(sessionID);

        Integer paidSum = database.selectPaidSum(sessionID);

        if (betPlaced) {
            showSummary();
            updateBalanceOnResult(event);
            betPlaced = false;
            updateBalance(event);
        }
        else {
            betInfo.setText("No bets were placed this round");
            betInfo.setTextFill(Color.WHITE);
        }
        database.updateRouletteSession(paidSum, sessionID);
    }

    @FXML
    private RadioButton rb5;
    @FXML
    private RadioButton rb10;
    @FXML
    private RadioButton rb25;
    @FXML
    private RadioButton rb50;
    @FXML
    private RadioButton rbMaxBet;
    @FXML
    private Label betInfo;
    public String pressedButton(ActionEvent event) {
        Button buttonPressed = (Button) event.getSource();
        betInfo.setText("You have placed a bet on: " + buttonPressed.getText());
        betInfo.setTextFill(Color.WHITE);
        return buttonPressed.getText();
    }
    public Integer checkBetSum() throws SQLException {
        if (rb5.isSelected())
            return 5;
        else if (rb10.isSelected())
            return 10;
        else if (rb25.isSelected())
            return 25;
        else if (rb50.isSelected())
            return 50;
        else if (rbMaxBet.isSelected()) {
            int aux = database.selectOldBalance(userID);
            if (aux != 0 && aux > 0) {
                return aux;
            }
            else return -2;
        }
        return -1;
    }

    public void updateBalanceOnBet(ActionEvent event) throws SQLException {
        Integer sumBet = database.selectSumBet(sessionIDBet, betID);
        Integer oldBalance = database.selectOldBalance(userID);

        database.updateBalanceOnBetDB(oldBalance, sumBet, userID);
        setBalance(sumBet);
    }

    public void updateBalanceOnResult(ActionEvent event) throws SQLException {
        Integer wonSum = database.selectWonSum(sessionIDResult);
        Integer nrOfBets = database.selectNrOfPendingBets(sessionIDResult);
        Integer oldSum = 0, newSum = 0;

        if (nrOfBets > 0) {
            oldSum = database.selectOldSum(userID);
            newSum = oldSum + wonSum;
            database.updateBalance(newSum, userID);
            setBalance(newSum);
        }
    }

    public void placeBet(ActionEvent event) throws SQLException {
        if (checkBetSum() == -1) {
            betInfo.setText("Please select a sum");
            betInfo.setTextFill(Color.WHITE);

        } else if (checkBetSum() == -2) {
            betInfo.setText("Insufficient funds. Make a deposit in TRANSACTIONS to play");
            betInfo.setTextFill(Color.RED);

        } else {
            boolean ok = true;
            Integer balance = database.selectOldBalance(userID);

            if (balance < checkBetSum()) {
                betInfo.setText("Insufficient funds. Make a deposit in TRANSACTIONS to play");
                betInfo.setTextFill(Color.WHITE);
                ok = false;
            }

            if (ok) {
                database.insertInRouletteBets(pressedButton(event), userID, checkBetSum());
                betPlaced = true;

                Integer check = database.checkk();
                Integer sessionID = database.selectSessionId();

                if (check != null) {
                    database.insertNull();
                }

                sessionID = database.selectSessionId();
                setSessionIDBet(sessionID);

                Integer maxBetId = database.selectMaxBetID();
                ////////
                database.updateRouletteBets(sessionID, maxBetId);

                Integer betId = database.selectBetID(sessionID);
                setBetId(betId);

                updateBalanceOnBet(event);

                Integer newBalance = database.selectOldBalance(userID);
                setBalance(newBalance);
            }
        }
    }

    @FXML
    Label balanceLabel;
    Integer balance;
    public void setBalance(Integer balance) {
        this.balance = balance;
        balanceLabel.setText(balance.toString());
    }

    public void updateBalance(ActionEvent event) {
        balanceLabel.setText(balance.toString());
    }
    Integer betID;
    public void setBetId(Integer betID) {
        this.betID = betID;
    }
    public void logout(ActionEvent event) throws IOException {
        SceneSwitcher.switchScene(event, "login.fxml");
    }
    public void transaction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
        Parent root = loader.load();
        TransactionController TransactionController = loader.getController();
        TransactionController.setUserID(userID);
        TransactionController.refresh(event);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void setBalanceText(Integer userID) throws SQLException {
        balanceLabel.setText(database.selectBalanceWelcome(userID));
    }

    public void setWelcomeText (ActionEvent event) throws SQLException {
        betInfo.setText("Welcome back, " + database.selectUsername(userID));
        betInfo.setTextFill(Color.GREEN);
    }

    @FXML
    private Label timeLabel;
    public void setTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLabel.setText("Time: " + LocalDateTime.now().format(formatter));
            }
        };
        timer.start();
    }
    public void showSummary() throws SQLException {

        Integer nrOfBets = database.selectNrOfBetsSummary(userID, sessionIDResult);
        Integer sumBet = database.selectSumBetSummary(userID, sessionIDResult);
        Integer sumWon = database.selectSumWonSummary(userID, sessionIDResult);
        Integer sumLost = database.selectSumLostSummary(userID, sessionIDResult);

        if (sumWon > abs(sumLost)) {
            betInfo.setText("You have placed " + nrOfBets + " bet(s). You bet " + sumBet + " and you won " + (sumWon - abs(sumLost)));
            betInfo.setTextFill(Color.GREEN);
        } else if (sumWon < abs(sumLost)) {
            betInfo.setText("You have placed " + nrOfBets + " bet(s). You bet " + sumBet + " and you lost " + (abs(sumLost) - sumWon));
            betInfo.setTextFill(Color.RED);
        } else if (sumWon == abs(sumLost)) {
            betInfo.setText("You have placed " + nrOfBets + " bet(s). You won 0 and lost 0.");
            betInfo.setTextFill(Color.WHITE);
        }
    }
}
