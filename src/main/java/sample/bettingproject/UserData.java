package sample.bettingproject;

public class UserData {
    private Integer userid;
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String cnp;
    private String address;
    private Integer balance;
    private Boolean banstatus;

    public UserData(Integer userid, String username, String email, String password, String firstname, String lastname, String cnp, String address, Integer balance, Boolean banstatus) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.cnp = cnp;
        this.address = address;
        this.balance = balance;
        this.banstatus = banstatus;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getCnp() {
        return cnp;
    }

    public void setCnp(String cnp) {
        this.cnp = cnp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getBanstatus() {
        return banstatus;
    }

    public void setBanstatus(Boolean banstatus) {
        this.banstatus = banstatus;
    }
}
