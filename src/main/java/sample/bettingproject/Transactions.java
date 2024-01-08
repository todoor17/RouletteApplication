package sample.bettingproject;

public class Transactions {
    private Integer transactionid;
    private Integer userid;
    private Integer cardid;
    private String typeoftransaction;
    private Integer value;
    private String dataoftransaction;

    public Transactions(Integer transactionid, Integer userid, Integer cardid, String typeoftransaction, Integer value, String dataoftransaction) {
        this.transactionid = transactionid;
        this.userid = userid;
        this.cardid = cardid;
        this.typeoftransaction = typeoftransaction;
        this.value = value;
        this.dataoftransaction = dataoftransaction;
    }

    public Integer getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(Integer transactionid) {
        this.transactionid = transactionid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCardid() {
        return cardid;
    }

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public String getTypeoftransaction() {
        return typeoftransaction;
    }

    public void setTypeoftransaction(String typeoftransaction) {
        this.typeoftransaction = typeoftransaction;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDataoftransaction() {
        return dataoftransaction;
    }

    public void setDataoftransaction(String dataoftransaction) {
        this.dataoftransaction = dataoftransaction;
    }
}
