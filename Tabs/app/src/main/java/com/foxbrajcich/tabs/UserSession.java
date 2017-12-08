package com.foxbrajcich.tabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Robert on 11/28/2017.
 */

public class UserSession {

    private static String username;
    private static String name;
    private static List<User> friends;
    private static List<Group> groupsList;

    static {
        friends = new ArrayList<>();
        groupsList = new ArrayList<>();
    }

    private UserSession(){}

    public static void clearUserSession(){
        username = null;
        name = null;
        friends = new ArrayList<>();
        groupsList = new ArrayList<>();
    }

    public static void setName(String name){
        UserSession.name = name;
    }

    public static String getName(){
        return name;
    }

    public static void setUsername(String username){UserSession.username = username;}

    public static String getUsername(){
        return username;
    }

    public static List<User> getFriends(){
        return friends;
    }

    public static void addFriend(User friend){
        friends.add(friend);
    }

    public static List<Group> getGroupsList() { return groupsList; }

    public static void addGroup(Group g) { groupsList.add(g); }

    public static void refreshGroups(OnDataFetchCompleteListener listener){

        final OnDataFetchCompleteListener completeListener = listener;

        FirebaseDatabase.getInstance().getReference("groups").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupsList.clear();

                if(!dataSnapshot.exists()){
                    return;
                }

                Map<String, Object> groups = (Map<String, Object>) dataSnapshot.getValue();

                for(String groupId : groups.keySet()){
                    Map<String, Object> groupInfo = (Map<String, Object>) groups.get(groupId);
                    String groupName = (String) groupInfo.get("name");

                    int groupIconId = 0;
                    if(groupInfo.containsKey("groupIcon")){
                        groupIconId = ((java.lang.Number) groupInfo.get("groupIcon")).intValue();
                    }

                    Map<String, Object> expenses = (Map<String, Object>) groupInfo.get("expenses");
                    Map<String, Object> members = (Map<String, Object>) groupInfo.get("members");
                    Map<String, Object> transactions = (Map<String, Object>) groupInfo.get("transactions");

                    List<Expense> expenseList = new ArrayList<>();
                    List<User> memberList = new ArrayList<>();
                    List<Transaction> transactionList = new ArrayList<>();

                    if(expenses != null && expenses.size() > 0) {
                        for (String expenseId : expenses.keySet()) {
                            Map<String, Object> expense = (Map<String, Object>) expenses.get(expenseId);

                            String content = (String) expense.get("content");
                            String username = (String) expense.get("username");
                            String usersName = (String) expense.get("usersName");
                            double amount = ((java.lang.Number) expense.get("amount")).doubleValue();

                            Expense newExpense = new Expense(content, usersName, amount);
                            newExpense.setUsername(username);

                            expenseList.add(newExpense);
                        }
                    }

                    boolean containsThisUser = false;

                    if(members != null && members.size() > 0) {
                        for (String memberId : members.keySet()) {
                            Map<String, Object> member = (Map<String, Object>) members.get(memberId);

                            String username = (String) member.get("username");
                            String name = (String) member.get("name");

                            if(username.toLowerCase().equals(UserSession.getUsername()))
                                containsThisUser = true;

                            User newUser = new User(name, username);
                            if(username.length() > 0) {
                                UserDataFetcher.registerUserToPopulate(newUser);
                            }
                            memberList.add(newUser);
                        }
                    }

                    if(!containsThisUser){
                        continue;
                    }

                    if(transactions != null && transactions.size() > 0) {
                        for (String transactionId : transactions.keySet()) {

                            Map<String, Object> transaction = (Map<String, Object>) transactions.get(transactionId);

                            String content = (String) transaction.get("content");
                            String sendingUser = (String) transaction.get("sendingUser");
                            String receivingUser = (String) transaction.get("receivingUser");
                            String sendingUsersName = (String) transaction.get("sendingUsersName");
                            String receivingUsersName = (String) transaction.get("receivingUsersName");
                            double amount = ((java.lang.Number) transaction.get("amount")).doubleValue();

                            Transaction newTransaction = new Transaction();
                            newTransaction.setName(content);
                            newTransaction.setReceivingUsername(receivingUser);
                            newTransaction.setSendingUsername(sendingUser);
                            newTransaction.setReceivingUsersName(receivingUsersName);
                            newTransaction.setSendingUsersName(sendingUsersName);
                            newTransaction.setAmount(amount);

                            transactionList.add(newTransaction);
                        }
                    }

                    Group groupToAdd = new Group(groupName, groupId, memberList, expenseList, transactionList, true);
                    groupToAdd.setGroupIconId(groupIconId);
                    UserSession.addGroup(groupToAdd);

                }

                completeListener.onDataFetchComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
