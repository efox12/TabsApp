package com.foxbrajcich.tabs;


import android.support.v4.widget.SwipeRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikfox on 11/15/17.
 */

public class Group implements Serializable{

    private String groupTitle;
    private String groupId;
    private List<User> members;
    private int groupIconId;
    List<Expense> expenses = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();
    private boolean isOnline;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Group(){}

    public Group(String groupTitle, String groupId, List<User> members, List<Expense> expenses, List<Transaction> transactions, boolean isOnline){
        this.groupTitle = groupTitle;
        this.groupId = groupId;
        this.members = members;
        this.expenses = expenses;
        this.transactions = transactions;
        this.isOnline = isOnline;
    }

    public void setGroupIconId(int groupIconId) { this.groupIconId = groupIconId; }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getGroupIconId() {
        return groupIconId;
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Debt> getDebtsForUser(User user){
        if(!members.contains(user)){
            return null;
        }

        List<Debt> debtList = new ArrayList<>();

        for(User currentDebtor : members){
            if(currentDebtor != user) {
                Debt debt = new Debt(currentDebtor);
                double owedToDebtor = 0d, owedByDebtor = 0d;


                //total the expenses that this user owes to the given debtor
                for(Expense expense : expenses){
                    if(isSameUser(expense.getUsername(), expense.getUsersName(), currentDebtor)) {
                        owedToDebtor += expense.getAmount() / members.size();
                    }else if(isSameUser(expense.getUsername(), expense.getUsersName(), user)){
                        owedByDebtor += expense.getAmount() / members.size();
                    }
                }

                //subtract payments made to the debtor from the debt and add payments received
                for(Transaction transaction : transactions){
                    if(isSameUser(transaction.getReceivingUsername(), transaction.getReceivingUsersName(), currentDebtor) &&
                            isSameUser(transaction.getSendingUsername(), transaction.getSendingUsersName(), user)){
                        owedToDebtor -= transaction.getAmount();
                    }else if(isSameUser(transaction.getReceivingUsername(), transaction.getReceivingUsersName(), user) &&
                            isSameUser(transaction.getSendingUsername(), transaction.getSendingUsersName(), currentDebtor)){
                        owedByDebtor -= transaction.getAmount();
                    }
                }


                if(owedToDebtor < owedByDebtor){
                    //the other person owes more money to this user than they do to them.
                    //Return 0 for the amount owed by this user and the difference to the other user.
                    debt.setAmount(0d);
                }else{
                    debt.setAmount(owedToDebtor - owedByDebtor);
                }

                debtList.add(debt);

            }
        }

        return debtList;

    }

    public double getTotalExpenseForUser(User user){
        double sum = 0d;

        for(Expense expense: expenses){
            if(isSameUser(expense.getUsername(), expense.getUsersName(), user)){
                sum += expense.getAmount();
            }
        }

        return sum;
    }

    public double getTotalContributionForUser(User user){
        double sum = 0d;

        for(Transaction transaction: transactions){
            if(isSameUser(transaction.getSendingUsername(), transaction.getSendingUsersName(), user)){
                sum += transaction.getAmount();
            } else if(isSameUser(transaction.getReceivingUsername(), transaction.getReceivingUsersName(), user)){
                sum -= transaction.getAmount();
            }
        }

        sum += getTotalExpenseForUser(user);
        return sum;
    }


    private boolean isSameUser(String username, String name, User user){

        if(username.equals(user.getUsername())){
            if(username.length() < 1){ //if both users are offline
                return name.equals(user.getName());
            }else{
                return true;
            }
        }

        return false;

    }

    public Debt getDebtToUser(List<Debt> debts, User user){

        for(Debt debt : debts){
            if(isSameUser(user.getUsername(), user.getName(), debt.getDebtor())){
                return debt;
            }
        }

        return null;
    }
}
