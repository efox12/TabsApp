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

    public String getName() {
        return name;
    }
}
