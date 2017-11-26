package com.foxbrajcich.tabs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class Group implements Serializable{
    private String groupTitle;
    private int groupId;
    private List<User> members;
    List<Expense> expenses = new ArrayList<>();
    private Hashtable<Integer, List<Expense>> expenseTable;
    private boolean isOnline;

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
