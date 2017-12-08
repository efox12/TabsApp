package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by erikfox on 11/15/17.
 */

public class Expense implements Serializable{
    private String content;
    private String usersName;
    private String username;
    private int expenseId;
    private double amount;
    private int userId;
    private Date dateAdded;

    public Expense(){
        this.username = "";
    }

    public Expense(String content, String usersName, double amount){
        this.content = content;
        this.usersName = usersName;
        this.username = "";
        this.amount = amount;
    }

    public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }

    public Date getDateAdded() { return dateAdded; }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getUsersName(){
        return usersName;
    }

    public void setUsersName(String usersName) {
        this.usersName = usersName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
