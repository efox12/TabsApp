package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class User implements Serializable{
    private String name;
    private String username;
    private int userId;
    private int amountPaid;
    private int amountOwed;
    private List<User> friends;
    private List<Expense> expenses;
    private boolean isOnline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}