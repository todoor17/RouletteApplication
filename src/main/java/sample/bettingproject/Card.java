package sample.bettingproject;

public class Card {
    private Integer cardID;
    private Integer userID;
    private String cardNumber;
    private String cvv;
    private Integer cardAvailableFunds;

    public Card(Integer cardID, Integer userID, String cardNumber, String cvv, Integer cardAvailableFunds) {
        this.cardID = cardID;
        this.userID = userID;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.cardAvailableFunds = cardAvailableFunds;
    }

    public Integer getCardID() {
        return cardID;
    }

    public void setCardID(Integer cardID) {
        this.cardID = cardID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Integer getCardAvailableFunds() {
        return cardAvailableFunds;
    }

    public void setCardAvailableFunds(Integer cardAvailableFunds) {
        this.cardAvailableFunds = cardAvailableFunds;
    }
}
