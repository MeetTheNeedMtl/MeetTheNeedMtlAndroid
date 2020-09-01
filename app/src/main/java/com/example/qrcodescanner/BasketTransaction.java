package com.example.qrcodescanner;

public class BasketTransaction {

    private String firstName;
    private String lastName;
    private String birthday;
    private String numBaskets;
    private Integer id;
    private String residenceyProofStatus;
    private String studentStatus;
    private Double balance;
    private Boolean livraison;
    private Boolean depannage;
    private Boolean christmasBasket;
    private String success;


    public BasketTransaction() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNumBaskets() {
        return numBaskets;
    }

    public void setNumBaskets(String numBaskets) {
        this.numBaskets = numBaskets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResidenceyProofStatus() {
        return residenceyProofStatus;
    }

    public void setResidenceyProofStatus(String residenceyProofStatus) {
        this.residenceyProofStatus = residenceyProofStatus;
    }

    public String getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getLivraison() {
        return livraison;
    }

    public void setLivraison(Boolean livraison) {
        this.livraison = livraison;
    }

    public Boolean getDepannage() {
        return depannage;
    }

    public void setDepannage(Boolean depannage) {
        this.depannage = depannage;
    }

    public Boolean getChristmasBasket() {
        return christmasBasket;
    }

    public void setChristmasBasket(Boolean christmasBasket) {
        this.christmasBasket = christmasBasket;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
