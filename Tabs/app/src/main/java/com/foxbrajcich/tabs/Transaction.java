package com.foxbrajcich.tabs;

import java.io.Serializable;

/**
 * Created by erikfox on 11/24/17.
 */

public class Transaction implements Serializable{
    private String name;
    private int transactionId;
    private double amount;
    private int sendingUserId;
    private int receivingUserId;
    private String sendingUserName;
    private String receivingUserName;

    public String getName() {
        return name;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getSendingUserId() {
        return sendingUserId;
    }

    public void setSendingUserId(int sendingUserId) {
        this.sendingUserId = sendingUserId;
    }

    public int getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(int receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public String getSendingUserName() {
        return sendingUserName;
    }

    public void setSendingUserName(String sendingUserName) {
        this.sendingUserName = sendingUserName;
    }

    public String getReceivingUserName() {
        return receivingUserName;
    }

    public void setReceivingUserName(String receivingUserName) {
        this.receivingUserName = receivingUserName;
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
