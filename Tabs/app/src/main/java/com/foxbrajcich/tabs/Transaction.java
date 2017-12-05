package com.foxbrajcich.tabs;

import java.io.Serializable;

/**
 * Created by erikfox on 11/24/17.
 */

public class Transaction implements Serializable{
    private String name;
    private int transactionId;
    private double amount;
    private String sendingUsername;
    private String receivingUsername;
    private String sendingUsersName;
    private String receivingUsersName;

    public String getName() {
        return name;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSendingUsername() {
        return sendingUsername;
    }

    public void setSendingUsername(String sendingUsername) {
        this.sendingUsername = sendingUsername;
    }

    public String getReceivingUsername() {
        return receivingUsername;
    }

    public void setReceivingUsername(String receivingUsername) {
        this.receivingUsername = receivingUsername;
    }

    public String getSendingUsersName() {
        return sendingUsersName;
    }

    public void setSendingUsersName(String sendingUsersName) {
        this.sendingUsersName = sendingUsersName;
    }

    public String getReceivingUsersName() {
        return receivingUsersName;
    }

    public void setReceivingUsersName(String receivingUsersName) {
        this.receivingUsersName = receivingUsersName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
