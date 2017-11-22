package com.foxbrajcich.tabs;

import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class User {
    private String name;
    private String username;
    private int userId;
    private int amountPaid;
    private int amountOwed;
    private List<User> friends;
    private List<Expense> expenses;
}