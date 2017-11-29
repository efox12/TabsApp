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
    private List<Transaction> transactions = new ArrayList<>();
    private boolean isOnline;

    public Group(){}

    public Group(String groupTitle, int groupId, List<User> members, List<Expense> expenses, List<Transaction> transactions, boolean isOnline){
        this.groupTitle = groupTitle;
        this.groupId = groupId;
        this.members = members;
        this.expenses = expenses;
        this.transactions = transactions;
        this.isOnline = isOnline;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
                    if(expense.getUserName().equals(currentDebtor.getName())){
                        owedToDebtor += expense.getAmount() / members.size();
                    }else if(expense.getUserName().equals(user.getName())){
                        owedByDebtor += expense.getAmount() / members.size();
                    }
                }

                //subtract payments made to the debtor from the debt and add payments received
                for(Transaction transaction : transactions){
                    if(transaction.getReceivingUserName().equals(currentDebtor.getName()) && transaction.getSendingUserName().equals(user.getName())){
                        owedToDebtor -= transaction.getAmount();
                    }else if(transaction.getReceivingUserName().equals(user.getName()) && transaction.getSendingUserName().equals(currentDebtor.getName())){
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
}
