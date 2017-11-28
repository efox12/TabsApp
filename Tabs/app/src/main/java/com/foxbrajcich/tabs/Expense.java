package com.foxbrajcich.tabs;

import java.io.Serializable;

/**
 * Created by erikfox on 11/15/17.
 */

public class Expense implements Serializable{
    private String content;
    private int expenseId;
    private double amount;
    private boolean completed;
    private int userId;
    private String username;

    public Expense(){}

    public Expense(String content, String username, double amount){
        this.content = content;
        this.username = username;
        this.amount = amount;
    }

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

    public String getUsername(){ return username; }

    public void payPartial(){

    }

    public void payFull(){

    }

    public void setCompleted(){

    }

}
