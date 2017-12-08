package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.Date;

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
    private String groupName;
    private Date dateAdded;
    public Transaction(){
        sendingUsername = "";
        receivingUsername = "";
        sendingUsersName = "";
        receivingUsersName = "";
    }

    public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }

    public Date getDateAdded() { return dateAdded; }

    public String getName() {
        return name;
    }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

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
