package sample.bettingproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DBConnector {
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:YOUR_PORT/bettingdatabase";
        String user = "YOUR_USERNAME";
        String password = "YOUR_PASSWORD";
        return DriverManager.getConnection(url, user, password);
    }

    //Sign up methods
    public void insertData(String username, String email, String password, String firstname, String lastname, String cnp, String address, String balance) throws SQLException {
        Connection connection = getConnection();
        String insert = "insert into userdata (username, email, passwordd, firstname, lastname, cnp, address, balance, banstatus) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(insert);
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, password);
        statement.setString(4, firstname);
        statement.setString(5, lastname);
        statement.setString(6, cnp);
        statement.setString(7, address);
        statement.setString(8, balance);
        statement.setBoolean(9, false);
        statement.executeUpdate();
    }

    public boolean checkInfo(Integer usernameOrEmailOrPhoneOrCNP, String string) throws SQLException {
        Connection connection = getConnection();
        String verify = null;
        //0 for username, 1 for email, 2 for CNP
        if (usernameOrEmailOrPhoneOrCNP == 0) {
            verify = "select count(userid) from userdata where username = ?;";
        } else if (usernameOrEmailOrPhoneOrCNP == 1) {
            verify = "select count(userid) from userdata where email = ?;";
        } else if (usernameOrEmailOrPhoneOrCNP == 2) {
            verify = "select count(userid) from userdata where CNP = ?;";
        }
        PreparedStatement statement = connection.prepareStatement(verify);
        statement.setString(1, string);
        ResultSet resultset = statement.executeQuery();
        if (resultset.next()) {
            if (resultset.getInt(1) == 0)
                return true;
        }
        return false;
    }

    //Login methods
    public int selectUser(String username, String password) throws SQLException {
        Connection connection = getConnection();
        String querry = "select userid from userdata where username = ? and passwordd = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getInt(1) > 0) {
                return resultSet.getInt(1);
            }
        }
        return -1;
    }

    public Integer selectBalance(String username, String password) throws SQLException {
        Connection connection = getConnection();
        String querry1 = "select balance from userdata where username = ? and passwordd = ?;";
        PreparedStatement statement1 = connection.prepareStatement(querry1);
        statement1.setString(1, username);
        statement1.setString(2, password);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public boolean checkUserExists(String username) throws SQLException {
        Connection connection = getConnection();
        String query0 = "select count(userid) from userdata where username = ?;";
        PreparedStatement statement0 = connection.prepareStatement(query0);
        statement0.setString(1, username);
        ResultSet resultSet0 = statement0.executeQuery();
        while (resultSet0.next()) {
            if (resultSet0.getInt(1) == 1)
                return true;
        }
        return false;
    }

    public boolean checkBan(String username) throws SQLException {
        Connection connection = getConnection();
        String query = "select banstatus from userdata where username = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getBoolean(1);
        }
        return false;
    }

    //Admin methods
    public ObservableList<UserData> fillUserDataList() throws SQLException {
        Connection connection = getConnection();
        ObservableList<UserData> userdataList = FXCollections.observableArrayList();
        String query = "select * from userdata";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Integer userid = resultSet.getInt(1);
            String username = resultSet.getString(2);
            String email = resultSet.getString(3);
            String password = resultSet.getString(4);
            String firstname = resultSet.getString(5);
            String lastname = resultSet.getString(6);
            String cnp = resultSet.getString(7);
            String address = resultSet.getString(8);
            Integer balance = resultSet.getInt(9);
            Boolean banstatus = resultSet.getBoolean(10);
            UserData userData = new UserData(userid, username, email, password, firstname, lastname, cnp, address, balance, banstatus);
            userdataList.add(userData);
        }
        return userdataList;
    }

    public ObservableList<Transactions> fillTransaction(String username) throws SQLException {
        Connection connection = getConnection();
        ObservableList<Transactions> history = FXCollections.observableArrayList();
        String query = "select * from transactions where userid = (select userid from userdata where username = ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Integer transactionid = resultSet.getInt(1);
            Integer userid = resultSet.getInt(2);
            Integer cardid = resultSet.getInt(3);
            String typeoftransaction = resultSet.getString(4);
            Integer value = resultSet.getInt(5);
            String dataoftransaction = resultSet.getString(6);
            Transactions transaction = new Transactions(transactionid, userid, cardid, typeoftransaction, value, dataoftransaction);
            history.add(transaction);
        }
        return history;
    }

    public void banUser(String username) throws SQLException {
        Connection connection = getConnection();
        String query = "update userdata set banstatus = ? where username = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setBoolean(1, true);
        statement.setString(2, username);
        statement.executeUpdate();
    }

    public void UnbanUser(String username) throws SQLException {
        Connection connection = getConnection();
        String query = "update userdata set banstatus = ? where username = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setBoolean(1, false);
        statement.setString(2, username);
        statement.executeUpdate();
    }

    public Integer selectNrOfBetsAdmin(String username) throws SQLException {
        Connection connection = getConnection();
        String query = "select count(betid) from roulettebets where userid = (select userid from userdata where username = ?);";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectSumBetAdmin(String username) throws SQLException {
        Connection connection = getConnection();
        String query1 = "select sum(amount) from roulettebets where userid = (select userid from userdata where username = ?);";
        PreparedStatement statement1 = connection.prepareStatement(query1);
        statement1.setString(1, username);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public Integer selectSumWonAdmin(String username) throws SQLException {
        Connection connection = getConnection();
        String query2 = "select sum(actualwin) from roulettebets where userid = (select userid from userdata where username = ?) and statuss = 'won';";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        statement2.setString(1, username);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()) {
            return resultSet2.getInt(1);
        }
        return -1;
    }

    public Integer selectSumLostAdmin(String username) throws SQLException {
        Connection connection = getConnection();
        String query3 = "select sum(actualwin) from roulettebets where userid = (select userid from userdata where username = ?) and statuss = 'lost';";
        PreparedStatement statement3 = connection.prepareStatement(query3);
        statement3.setString(1, username);
        ResultSet resultSet3 = statement3.executeQuery();
        while (resultSet3.next()) {
            return resultSet3.getInt(1);
        }
        return -1;
    }

    //Transaction methods
    public ObservableList<Card> fillCards(String user) throws SQLException {
        ObservableList<Card> cards = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String querry = "select * from availablecards where userid = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, user);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Integer cardID = resultSet.getInt("cardID");
            Integer userID = resultSet.getInt("userid");
            Integer availablefunds = resultSet.getInt("cardavailablefunds");
            String cardnumber = resultSet.getString("cardnumber");
            String cvv = resultSet.getString("cvv");
            Card card = new Card(cardID, userID, cardnumber, cvv, availablefunds);
            cards.add(card);
        }
        return cards;
    }

    public Integer selectBalanceTransaction(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query = "select balance from userdata where userid = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public void addCard(String user, String cardnumber, String cvv) throws SQLException {
        Connection connection = getConnection();
        Random rand = new Random();
        Integer randvalues = rand.nextInt(500) + 100;
        String querry = "insert into availablecards(userID, cardnumber, cvv, cardavailablefunds) values (?, ?, ?, ?);";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, user);
        statement.setString(2, cardnumber);
        statement.setString(3, cvv);
        statement.setString(4, randvalues.toString());
        statement.executeUpdate();
    }

    public Integer selectCardFunds(String cardnumber) throws SQLException {
        Connection connection = getConnection();
        String querry = "select cardavailablefunds from availablecards where cardnumber = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, cardnumber);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public void updateFunds(String availableFunds, String cardNumber) throws SQLException {
        Connection connection = getConnection();
        String querry2 = "update availablecards set cardavailableFunds = ? where cardnumber = ?";
        PreparedStatement statement1 = connection.prepareStatement(querry2);
        statement1.setString(1, availableFunds.toString());
        statement1.setString(2, cardNumber);
        statement1.executeUpdate();
    }

    public Integer selectBalanceTransaction(String cardNumber) throws SQLException {
        Connection connection = getConnection();
        String querry3 = "select balance, u.userid from userdata u \n" +
                "join availablecards a on u.userid = a.userid\n" +
                "where cardnumber = ?;";
        PreparedStatement statement2 = connection.prepareStatement(querry3);
        statement2.setString(1, cardNumber);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()) {
            return resultSet2.getInt(1);
        }
        return -1;
    }

    public Integer selectUserIDTransaction(String cardNumber) throws SQLException {
        Connection connection = getConnection();
        String querry3 = "select balance, u.userid from userdata u \n" +
                "join availablecards a on u.userid = a.userid\n" +
                "where cardnumber = ?;";
        PreparedStatement statement2 = connection.prepareStatement(querry3);
        statement2.setString(1, cardNumber);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()) {
            return resultSet2.getInt(2);
        }
        return -1;
    }

    public void updateBalanceTransaction(String balance, String userID) throws SQLException {
        Connection connection = getConnection();
        String querry4 = "update userdata set balance = ? where userid = ?";
        PreparedStatement statement3 = connection.prepareStatement(querry4);
        statement3.setString(1, balance);
        statement3.setString(2, userID);
        statement3.executeUpdate();
    }

    public Integer selectCardIDTransaction(String cardNumber) throws SQLException {
        Connection connection = getConnection();
        String querry5 = "select cardid from availablecards where cardnumber = ?";
        PreparedStatement statement4 = connection.prepareStatement(querry5);
        statement4.setString(1, cardNumber);
        ResultSet resultSet1 = statement4.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public void addTransactionDeposit(Integer userid, Integer cardID, String ammountString) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "insert into transactions (userid, cardid, typeoftransaction, value, dateoftransaction) values (?, ?, 'deposit', ?, ?);";
        PreparedStatement statement5 = connection.prepareStatement(querry6);
        statement5.setString(1, userid.toString());
        statement5.setString(2, cardID.toString());
        statement5.setString(3, ammountString);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        String dateS = dateFormat.format(date);
        statement5.setString(4, dateS);
        statement5.executeUpdate();
    }

    public void addTransactionWithdraw(Integer userid, Integer cardID, String ammountString) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "insert into transactions (userid, cardid, typeoftransaction, value, dateoftransaction) values (?, ?, 'withdraw', ?, ?);";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, userid.toString());
        statement6.setString(2, cardID.toString());
        statement6.setString(3, ammountString);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateS = dateFormat.format(date);
        statement6.setString(4, dateS);
        statement6.executeUpdate();
    }

    public boolean checkCardTransaction(String cardNumber, Integer userID) throws SQLException {
        Connection connection = getConnection();
        String querry = "select count(userID) from availablecards where cardnumber = ? and userid = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, cardNumber);
        statement.setInt(2, userID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            if (resultSet.getInt(1) == 1)
                return true;
        }
        return false;
    }

    public Integer selectCardFundsDeposit(String cardNumber, Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query = "select cardavailablefunds from availablecards funds where cardnumber = ? and userid = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, cardNumber);
        statement.setInt(2, userID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectBalance1(String cardNumber) throws SQLException {
        Connection connection = getConnection();
        String querry3 = "select balance, u.userid from userdata u \n" +
                "join availablecards a on u.userid = a.userid\n" +
                "where cardnumber = ?;";
        PreparedStatement statement2 = connection.prepareStatement(querry3);
        statement2.setString(1, cardNumber);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()) {
            return resultSet2.getInt(1);
        }
        return -1;
    }

    public Integer selectBalanceWithdraw(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query = "select balance from userdata where userid = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    //Roulette methods
    public Integer selectSessionId() throws SQLException {
        Connection connection = getConnection();
        String querry = "select MAX(sessionID) from roulettesession";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querry);
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectResultWhenMax(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry3 = "select result from roulettesession where sessionid = ?;";
        PreparedStatement statement3 = connection.prepareStatement(querry3);
        statement3.setInt(1, sessionID);
        ResultSet resultSet3 = statement3.executeQuery();
        if (resultSet3.next()) {
            Integer result = resultSet3.getInt(1);
            if (resultSet3.wasNull()) {
                return null;
            }
            return result;
        }
        return -1;
    }

    public Integer selectNrOfBets(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry1 = "select count(betid) from roulettebets where sessionid = ?;";
        PreparedStatement statement1 = connection.prepareStatement(querry1);
        statement1.setInt(1, sessionID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public Integer selectRecievedSum(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry1 = "select sum(amount) from roulettebets where sessionid = ?;";
        PreparedStatement statement1 = connection.prepareStatement(querry1);
        statement1.setInt(1, sessionID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public void updateRouletteSessionWhenBet(Integer result, Integer userID, Integer nrOfBets, Integer recievedSum, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry2 = "update roulettesession set result = ?, userid = ?, numberofbets = ?, recievedsum = ?, timee = ? where sessionid = ?;";
        PreparedStatement statement2 = connection.prepareStatement(querry2);
        statement2.setString(1, result.toString());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dates = df.format(date);
        statement2.setInt(2, userID);
        statement2.setInt(3, nrOfBets);
        statement2.setInt(4, recievedSum);
        statement2.setString(5, dates);
        statement2.setInt(6, sessionID);
        statement2.executeUpdate();
    }

    public void updateRouletteSessionWhenNoBet(Integer result, Integer userID) throws SQLException {
        Connection connection = getConnection();
        String querry2 = "insert into roulettesession (result, userid, numberofbets, recievedsum, paidsum, profit, timee) values (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement2 = connection.prepareStatement(querry2);
        statement2.setString(1, result.toString());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dates = df.format(date);
        statement2.setInt(2, userID);
        statement2.setString(3, "0");
        statement2.setString(4, "0");
        statement2.setString(5, "0");
        statement2.setString(6, "0");
        statement2.setString(7, dates);
        statement2.executeUpdate();
    }

    public void updateStatus(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry5 = "update roulettebets set statuss = 'lost' where sessionid = ?;";
        PreparedStatement statement5 = connection.prepareStatement(querry5);
        statement5.setInt(1, sessionID);
        statement5.executeUpdate();
    }

    public void updateWonRed(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 1 where typeofbet = 'Red' and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setInt(1, sessionID);
        statement6.executeUpdate();
    }

    public void updateWonBlack(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 2  where typeofbet = 'Black' and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setInt(1, sessionID);
        statement6.executeUpdate();
    }

    public void updateWonGreen(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 10  where typeofbet = '0' and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setInt(1, sessionID);
        statement6.executeUpdate();
    }

    public void updateWonNumber(Integer sessionID, Integer i) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 10  where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, i.toString());
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWon1st12(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 5 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "1st 12");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWon2nd12(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 6 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "2nd 12");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWon3rd12(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 7 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "3rd 12");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWon1_18(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 8 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "1-18");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWon19_36(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 9 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "19-36");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWonEven(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 4 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "Even");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateWonOdd(String typeOfBet, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set statuss = 'won', payoutid = 3 where typeofbet = ? and sessionid = ?;";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setString(1, "Odd");
        statement6.setInt(2, sessionID);
        statement6.executeUpdate();
    }

    public void updateActualWin2x(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry7 = "update roulettebets set actualwin = possiblewin / 2 where statuss = 'won' and sessionid = ? and (payoutid = 1 or payoutid = 2 or payoutid = 3 or payoutid = 4 or payoutid = 8 or payoutid = 9);";
        PreparedStatement statement7 = connection.prepareStatement(querry7);
        statement7.setInt(1, sessionID);
        statement7.executeUpdate();
    }

    public void updateActualWin3x(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry71 = "update roulettebets set actualwin = possiblewin / 3 * 2 where statuss = 'won' and sessionid = ? and (payoutid = 5 or payoutid = 6 or payoutid = 7);";
        PreparedStatement statement71 = connection.prepareStatement(querry71);
        statement71.setInt(1, sessionID);
        statement71.executeUpdate();
    }

    public void updateActualWin36x(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry72 = "update roulettebets set actualwin = possiblewin / 36 * 35 where statuss = 'won' and sessionid = ? and (payoutid = 10);";
        PreparedStatement statement72 = connection.prepareStatement(querry72);
        statement72.setInt(1, sessionID);
        statement72.executeUpdate();
    }

    public void updateActualWinLost(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry8 = "update roulettebets set actualwin = -amount where statuss = 'lost' and sessionid = ?";
        PreparedStatement statement8 = connection.prepareStatement(querry8);
        statement8.setInt(1, sessionID);
        statement8.executeUpdate();
    }

    public Integer selectPaidSum(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry9 = "select sum(actualwin) from roulettebets where sessionid = ?;";
        PreparedStatement statement9 = connection.prepareStatement(querry9);
        statement9.setInt(1, sessionID);
        ResultSet resultSet9 = statement9.executeQuery();
        while (resultSet9.next()) {
            return resultSet9.getInt(1);
        }
        return -1;
    }

    public void updateRouletteSession(Integer paidSum, Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry10 = "update roulettesession set paidsum = ?, profit = recievedsum - paidsum where sessionid = ?;";
        PreparedStatement statement10 = connection.prepareStatement(querry10);
        statement10.setInt(1, paidSum);
        statement10.setInt(2, sessionID);
        statement10.executeUpdate();
    }

    public Integer selectSumBet(Integer sessionIDBet, Integer betID) throws SQLException {
        Connection connection = getConnection();
        String querry = "select amount from roulettebets where sessionid = ? and betid = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setInt(1, sessionIDBet);
        statement.setInt(2, betID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectOldBalance(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String querry1 = "select balance from userdata where userid = ?;";
        PreparedStatement statement1 = connection.prepareStatement(querry1);
        statement1.setInt(1, userID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public void updateBalanceOnBetDB(Integer oldBalance, Integer sumBet, Integer userID) throws SQLException {
        Connection connection = getConnection();
        String querry2 = "update userdata set balance = ? - ? where userid = ?;";
        PreparedStatement statement2 = connection.prepareStatement(querry2);
        statement2.setInt(1, oldBalance);
        statement2.setInt(2, sumBet);
        statement2.setInt(3, userID);
        statement2.executeUpdate();
    }

    public Integer selectWonSum(Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query0 = "SELECT sum(possiblewin) FROM roulettebets WHERE sessionID = ? AND statuss = 'won';";
        PreparedStatement statement0 = connection.prepareStatement(query0);
        statement0.setInt(1, sessionIDResult);
        ResultSet resultSet0 = statement0.executeQuery();
        while (resultSet0.next()) {
            return resultSet0.getInt(1);
        }
        return -1;
    }

    public Integer selectNrOfPendingBets(Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query = "SELECT count(betid) FROM roulettebets WHERE sessionID = ? AND statuss <> 'pending';";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, sessionIDResult);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectOldSum(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query1 = "SELECT balance FROM userdata WHERE userid = ?";
        PreparedStatement statement1 = connection.prepareStatement(query1);
        statement1.setInt(1, userID);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public void updateBalance(Integer newSum, Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query2 = "UPDATE userdata SET balance = ? WHERE userID = ?";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        statement2.setInt(1, newSum);
        statement2.setInt(2, userID);
        statement2.executeUpdate();
    }

    public void insertInRouletteBets(String pressedButton, Integer userID, Integer checkBetSum) throws SQLException {
        Connection connection = getConnection();
        String querry = "insert into roulettebets (userid, typeofbet, payoutid, amount, possiblewin, statuss) values (?, ?, ?, ?, ?, 'pending');";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setInt(1, userID);
        statement.setString(2, pressedButton); //pressedButton(event)
        statement.setInt(4, checkBetSum);
        for (Integer i = 0; i <= 36; i++) {
            if (pressedButton.equals(i.toString())) {
                statement.setInt(3, 10);
                statement.setInt(5, 36 * checkBetSum);
                break;
            }
        }
        String pressedButtonValue = pressedButton;
        int betSum = checkBetSum;
        if (pressedButtonValue.equals("1st 12")) {
            statement.setInt(3, 5);
            statement.setInt(5, 3 * betSum);
        } else if (pressedButtonValue.equals("2nd 12")) {
            statement.setInt(3, 6);
            statement.setInt(5, 3 * betSum);
        } else if (pressedButtonValue.equals("3rd 12")) {
            statement.setInt(3, 7);
            statement.setInt(5, 3 * betSum);
        } else if (pressedButtonValue.equals("Odd")) {
            statement.setInt(3, 3);
            statement.setInt(5, 2 * betSum);
        } else if (pressedButtonValue.equals("Even")) {
            statement.setInt(3, 4);
            statement.setInt(5, 2 * betSum);
        } else if (pressedButtonValue.equals("Red")) {
            statement.setInt(3, 1);
            statement.setInt(5, 2 * betSum);
        } else if (pressedButtonValue.equals("Black")) {
            statement.setInt(3, 2);
            statement.setInt(5, 2 * betSum);
        } else if (pressedButtonValue.equals("1-18")) {
            statement.setInt(3, 8);
            statement.setInt(5, 2 * betSum);
        } else if (pressedButtonValue.equals("19-36")) {
            statement.setInt(3, 9);
            statement.setInt(5, 2 * betSum);
        }
        statement.executeUpdate();
    }

    public Integer checkk() throws SQLException {
        Connection connection = getConnection();
        String querry4 = "select result from roulettesession where sessionid = (select MAX(sessionid) from roulettesession);";
        Statement statement4 = connection.createStatement();
        ResultSet resultSet1 = statement4.executeQuery(querry4);
        if (resultSet1.next()) {
            Integer result = resultSet1.getInt(1);
            if (resultSet1.wasNull()) {
                return null;
            }
            return result;
        }
        return -1;
    }

    public void insertNull() throws SQLException {
        Connection connection = getConnection();
        String querry1 = "insert into roulettesession(result, numberofbets, recievedsum, paidsum, timee) values (null, null, null, null, null);";
        Statement statement1 = connection.createStatement();
        statement1.executeUpdate(querry1);
    }

    public Integer selectMaxBetID() throws SQLException {
        Connection connection = getConnection();
        String querry5 = "select max(betid) from roulettebets;";
        Statement statement5 = connection.createStatement();
        ResultSet resultSet2 = statement5.executeQuery(querry5);
        while (resultSet2.next()) {
            return resultSet2.getInt(1);
        }
        return -1;
    }

    public void updateRouletteBets(Integer sessionID, Integer maxBetID) throws SQLException {
        Connection connection = getConnection();
        String querry6 = "update roulettebets set sessionid = ? where betid = ?";
        PreparedStatement statement6 = connection.prepareStatement(querry6);
        statement6.setInt(1, sessionID);
        statement6.setInt(2, maxBetID);
        statement6.executeUpdate();
    }

    public Integer selectBetID(Integer sessionID) throws SQLException {
        Connection connection = getConnection();
        String querry8 = "select betid from roulettebets where sessionid = ?;";
        PreparedStatement statement8 = connection.prepareStatement(querry8);
        statement8.setInt(1, sessionID);
        ResultSet resultSet8 = statement8.executeQuery();
        while (resultSet8.next()) {
            return resultSet8.getInt(1);
        }
        return -1;
    }

    public String selectBalanceWelcome(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String querry = "select balance from userdata where userid = ?;";
        PreparedStatement statement = connection.prepareStatement(querry);
        statement.setString(1, userID.toString());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }

    public String selectUsername(Integer userID) throws SQLException {
        Connection connection = getConnection();
        String query = "select username from userdata where userid = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getString(1);
        }
        return "";
    }

    public Integer selectNrOfBetsSummary(Integer userID, Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query = "select count(betid) from roulettebets where userid = ? and sessionid = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setInt(2, sessionIDResult);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public Integer selectSumBetSummary(Integer userID, Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query1 = "select sum(amount) from roulettebets where userid = ? and sessionid = ?;";
        PreparedStatement statement1 = connection.prepareStatement(query1);
        statement1.setInt(1, userID);
        statement1.setInt(2, sessionIDResult);
        ResultSet resultSet1 = statement1.executeQuery();
        while (resultSet1.next()) {
            return resultSet1.getInt(1);
        }
        return -1;
    }

    public Integer selectSumWonSummary(Integer userID, Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query2 = "select sum(actualwin) from roulettebets where userid = ? and sessionid = ? and statuss = 'won';";
        PreparedStatement statement2 = connection.prepareStatement(query2);
        statement2.setInt(1, userID);
        statement2.setInt(2, sessionIDResult);
        ResultSet resultSet2 = statement2.executeQuery();
        while (resultSet2.next()) {
            return resultSet2.getInt(1);
        }
        return -1;
    }

    public Integer selectSumLostSummary(Integer userID, Integer sessionIDResult) throws SQLException {
        Connection connection = getConnection();
        String query3 = "select sum(actualwin) from roulettebets where userid = ? and sessionid = ? and statuss = 'lost';";
        PreparedStatement statement3 = connection.prepareStatement(query3);
        statement3.setInt(1, userID);
        statement3.setInt(2, sessionIDResult);
        ResultSet resultSet3 = statement3.executeQuery();
        while (resultSet3.next()) {
            return resultSet3.getInt(1);
        }
        return -1;
    }

    public Integer getLastResult () throws SQLException {
        Connection connection = getConnection();
        String query = "select result from roulettesession order by sessionid desc limit 1";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return -1;
    }

    public void updateResultsArrayOnLogin(Integer[] results) throws SQLException {
        Connection connection = getConnection();
        int index = 0;
        String selectPrev = "SELECT result FROM roulettesession where sessionid > (select max(sessionid) - 5 from roulettesession);";
        Statement statementPrev = connection.createStatement();
        ResultSet resultSetPrev = statementPrev.executeQuery(selectPrev);
        while (resultSetPrev.next() && index < 5) {
            results[index++] = resultSetPrev.getInt(1);
        }
    }

    public void updateResultsArrayOnGetResult(Integer[] results) throws SQLException {
        Connection connection = getConnection();
        int index = 0;
        String selectPrev = "SELECT result FROM roulettesession where sessionid > (select max(sessionid) - 4 from roulettesession);";
        Statement statementPrev = connection.createStatement();
        ResultSet resultSetPrev = statementPrev.executeQuery(selectPrev);
        while (resultSetPrev.next()) {
            int auxresult = resultSetPrev.getInt(1);
            if (resultSetPrev.wasNull()) {
                results[index] = -1;
            } else {
                results[index] = auxresult;
            }
            index++;
        }
    }

}