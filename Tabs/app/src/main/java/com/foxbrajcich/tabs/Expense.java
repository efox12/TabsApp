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

    public void payPartial(){

    }

    public void payFull(){

    }

    public void setCompleted(){

    }

}
