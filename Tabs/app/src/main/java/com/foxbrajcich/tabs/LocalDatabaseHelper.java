package com.foxbrajcich.tabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/27/2017.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static LocalDatabaseHelper mInstance = null;

    //global constants for the db
    static final String DATABASE_NAME = "databaseLocalGroups";
    static final int DATABASE_VERSION = 1;

    //table name constants
    static final String TABLE_GROUPS = "tableGroups";
    static final String TABLE_GROUP_USERS = "tableGroupUsers";
    static final String TABLE_EXPENSES = "tableExpenses";
    static final String TABLE_TRANSACTIONS = "tableTransactions";

    //table column constants
    static final String ID = "_id"; //used in multiple tables
    static final String NAME = "name";
    static final String USERNAME = "username";
    static final String GROUPID = "groupid";
    static final String AMOUNT = "amount";
    static final String FROM_USER = "fromUser";
    static final String TO_USER = "toUser";
    static final String GROUP_ICON_ID = "groupIconId";


    //make the database helper exist as one instance that can be accessed from multiple classes
    public static LocalDatabaseHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new LocalDatabaseHelper(context.getApplicationContext());
        }

        return mInstance;
    }

    //constructor
    private LocalDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Create the tables used for our database
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create the table of groups containing their id, name, and icon id
        String createGroupTable =
                "CREATE TABLE " + TABLE_GROUPS + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        GROUP_ICON_ID + " INTEGER)";

        db.execSQL(createGroupTable);

        //create the table of user-group memberships containing the users name and the group id they are tied to
        String createGroupUserTable =
                "CREATE TABLE " + TABLE_GROUP_USERS + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USERNAME + " TEXT, " +
                        GROUPID + " INTEGER)";

        db.execSQL(createGroupUserTable);

        //create the table of expenses including its name, the users name who created it, the groups id, and the amount
        String createExpenseTable =
                "CREATE TABLE " + TABLE_EXPENSES + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        USERNAME + " TEXT, " +
                        GROUPID + " INTEGER, " +
                        AMOUNT + " FLOAT)";

        db.execSQL(createExpenseTable);

        //create the table of Transactions which holds a name, to and from users names, amount, and the group id it is associated with
        String createTransactionTable =
                "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        FROM_USER + " TEXT, " +
                        TO_USER + " TEXT, " +
                        AMOUNT + " FLOAT, " +
                        GROUPID + " INTEGER)";

        db.execSQL(createTransactionTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //do nothing
    }

    /**
     * Returns a list of group members (User objects) given a group id
     * @param groupId the id of the group to get the members of
     * @return a list of User objects representing the group's members
     */
    public List<User> getGroupMembersById(int groupId){
        List<User> members = new ArrayList<>();

        String getGroupMembersQuery =
                "SELECT * FROM " + TABLE_GROUP_USERS + " WHERE " + GROUPID + "=" + groupId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroupMembersQuery, null);

        while(cursor.moveToNext()){

            String username = cursor.getString(1);

            User newUser = new User(username);
            members.add(newUser);
        }

        return members;
    }

    /**
     * This function gets all the expenses associated with a group given the group's id
     * @param groupId the id of the group whose expenses are to be returned
     * @return a list of Expense objects for each expense in this group
     */
    public List<Expense> getGroupExpensesById(int groupId){
        List<Expense> expenses = new ArrayList<>();

        String getGroupExpensesQuery =
                "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + GROUPID + "=" + groupId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroupExpensesQuery, null);

        while(cursor.moveToNext()){
            String name = cursor.getString(1);
            String username = cursor.getString(2);
            double amount = cursor.getDouble(4);

            Expense newExpense = new Expense(name, username, amount);

            expenses.add(newExpense);
        }

        return expenses;
    }

    /**
     * returns a list of all the offline groups stored in the local database
     * @return a list of Group objects of all offline groups
     */
    public List<Group> getAllOfflineGroups(){
        List<Group> groups = new ArrayList<>();

        String getGroupsQuery =
                "SELECT * FROM " + TABLE_GROUPS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroupsQuery, null);

        while(cursor.moveToNext()){
            int groupId = cursor.getInt(0);
            String groupName = cursor.getString(1);
            int groupIconId = cursor.getInt(2);

            Group newGroup = new Group(groupName, String.valueOf(groupId), getGroupMembersById(groupId),
                    getGroupExpensesById(groupId), getGroupTransactionsById(groupId), false);

            newGroup.setGroupIconId(groupIconId);

            groups.add(newGroup);
        }

        return groups;
    }

    /**
     * inserts the given users into the local database and ties them to the group whose id is given
     * @param members a list of users to add to the database and link to the group
     * @param groupId the id of the group that these users should be linked to
     */
    public void linkUsersToGroup(List<User> members, int groupId){

        if(members.size() < 1){
            return;
        }

        String linkUsersSql = "INSERT INTO " + TABLE_GROUP_USERS + " VALUES";

        for(int i = 0; i < members.size(); i++){
            if(i != 0) linkUsersSql += ","; //don't include a comma for the first set of values
            linkUsersSql += " (null, '" + members.get(i).getName() + "', " + groupId + ")";
        }

        linkUsersSql += ";";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(linkUsersSql);
    }

    /**
     * inserts the given expenses into the local database and ties them to the group whose id is given
     * @param expenses a list of expenses to add
     * @param groupId the group id of the group that these expenses are a part of
     */
    public void linkExpensesToGroup(List<Expense> expenses, int groupId){

        if(expenses.size() < 1){
            return;
        }

        String linkExpensesSql = "INSERT INTO " + TABLE_EXPENSES + " VALUES";

        for(int i = 0; i < expenses.size(); i++){
            Expense expense = expenses.get(0);

            if(i != 0) linkExpensesSql += ",";

            linkExpensesSql += " (null, '" + expense.getContent() + "', '" + expense.getUsersName()
                    + "', " + groupId + ", " + expense.getAmount() + ")";
        }

        linkExpensesSql += ";";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(linkExpensesSql);

    }

    /**
     * adds the given group to the database
     * @param group the group to add
     */
    public void addGroupToDatabase(Group group){

        String addGroupSql = "INSERT INTO " + TABLE_GROUPS + " VALUES(null, '" + group.getGroupTitle() + "', " + group.getGroupIconId() + ")";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(addGroupSql);

        String getIndexSql = "SELECT last_insert_rowid()";
        Cursor c = db.rawQuery(getIndexSql, null);

        c.moveToFirst();
        int groupId = (int) c.getLong(0);

        //store the group's assigned id into the object
        group.setGroupId(String.valueOf(groupId));

        linkUsersToGroup(group.getMembers(), groupId);
        linkExpensesToGroup(group.getExpenses(), groupId);
        linkTransactionsToGroup(group.getTransactions(), groupId);

    }

    /**
     * adds the given expense to the group given
     * @param group the group to add the expense to
     * @param expense the expense to add to the group
     */
    public void addExpenseToGroup(Group group, Expense expense){

        String addExpenseSql = "INSERT INTO " + TABLE_EXPENSES +
                " VALUES(null, '" + expense.getContent() + "', " +
                "'" + expense.getUsersName() + "', " + group.getGroupId() + ", " + expense.getAmount() + ")";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(addExpenseSql);

    }

    /**
     * adds the given transaction to the group given
     * @param group the group to add the transaction to
     * @param transaction the transaction to add to the group
     */
    public void addTransactionToGroup(Group group, Transaction transaction){
        List<Transaction> toAdd = new ArrayList<>();
        toAdd.add(transaction);
        linkTransactionsToGroup(toAdd, Integer.parseInt(group.getGroupId()));
    }

    /**
     * adds the given transactions to the group whose group id is given
     * @param transactions the list of transactions to be added to the database
     * @param groupId the id of the group that these transactions should be tied to
     */
    public void linkTransactionsToGroup(List<Transaction> transactions, int groupId){
        if(transactions.size() < 1){
            return;
        }

        String linkTransactionsSql = "INSERT INTO " + TABLE_TRANSACTIONS + " VALUES";

        for(int i = 0; i < transactions.size(); i++){
            Transaction transaction = transactions.get(0);

            if(i != 0) linkTransactionsSql += ",";

            linkTransactionsSql += " (null, '" + transaction.getName() + "', '" + transaction.getSendingUsersName()
                    + "', '" + transaction.getReceivingUsersName() + "', " + transaction.getAmount() + ", " + groupId + ")";
        }

        linkTransactionsSql += ";";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(linkTransactionsSql);
    }

    /**
     * returns the transactions for a group with the given group id
     * @param groupId the id of the group to get the transactions for
     * @return the list of transactions that are part of this group
     */
    public List<Transaction> getGroupTransactionsById(int groupId){
        List<Transaction> transactions = new ArrayList<>();


        String getGroupTransactionsQuery =
                "SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + GROUPID + "=" + groupId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroupTransactionsQuery, null);

        while(cursor.moveToNext()){

            Transaction transaction = new Transaction();

            //populate the new transaction object
            transaction.setTransactionId(cursor.getInt(0));
            transaction.setName(cursor.getString(1));
            transaction.setSendingUsersName(cursor.getString(2));
            transaction.setReceivingUsersName(cursor.getString(3));
            transaction.setAmount(cursor.getDouble(4));

            transactions.add(transaction);
        }

        return transactions;
    }


}
