package com.foxbrajcich.tabs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.StringBuilderPrinter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 11/27/2017.
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    //global constants for the db
    static final String DATABASE_NAME = "databaseLocalGroups";
    static final int DATABASE_VERSION = 1;

    //table name constants
    static final String TABLE_GROUPS = "tableGroups";
    static final String TABLE_GROUP_USERS = "tableGroupUsers";
    static final String TABLE_EXPENSES = "tableExpenses";

    //table column constants
    static final String ID = "_id"; //used in multiple tables
    static final String NAME = "name";
    static final String USERNAME = "username";
    static final String GROUPID = "groupid";
    static final String AMOUNT = "amount";

    public LocalDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create the table of groups
        String createGroupTable =
                "CREATE TABLE " + TABLE_GROUPS + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT)";

        db.execSQL(createGroupTable);

        //create the table of user-group memberships
        String createGroupUserTable =
                "CREATE TABLE " + TABLE_GROUP_USERS + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USERNAME + " TEXT, " +
                        GROUPID + " INTEGER)";

        db.execSQL(createGroupUserTable);

        //create the table of expenses
        String createExpenseTable =
                "CREATE TABLE " + TABLE_EXPENSES + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NAME + " TEXT, " +
                        USERNAME + " TEXT, " +
                        GROUPID + " INTEGER, " +
                        AMOUNT + " FLOAT)";

        db.execSQL(createExpenseTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //do nothing for now
    }

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

    public List<Group> getAllOfflineGroups(){
        List<Group> groups = new ArrayList<>();

        String getGroupsQuery =
                "SELECT * FROM " + TABLE_GROUPS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(getGroupsQuery, null);

        while(cursor.moveToNext()){
            int groupId = cursor.getInt(0);
            String groupName = cursor.getString(1);

            Group newGroup = new Group(groupName, groupId, getGroupMembersById(groupId), getGroupExpensesById(groupId), false);

            groups.add(newGroup);
        }

        return groups;
    }

    public void linkUsersToGroup(List<User> members, int groupId){

        String linkUsersSql = "INSERT INTO " + TABLE_GROUP_USERS;

        for(int i = 0; i < members.size(); i++){
            if(i != 0) linkUsersSql += ","; //don't include a comma for the first set of values
            linkUsersSql += " VALUES(null, '" + members.get(i).getName() + "', " + groupId + ")";
        }

        linkUsersSql += ";";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(linkUsersSql);
    }

    public void linkExpensesToGroup(List<Expense> expenses, int groupId){

        String linkExpensesSql = "INSERT INTO " + TABLE_EXPENSES;

        for(int i = 0; i < expenses.size(); i++){
            Expense expense = expenses.get(0);

            if(i != 0) linkExpensesSql += ",";

            linkExpensesSql += " VALUES(null, '" + expense.getContent() + "', '" + expense.getUserName()
                    + "', " + groupId + ", " + expense.getAmount() + ")";
        }

        linkExpensesSql += ";";

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(linkExpensesSql);

    }

    public void addGroupToDatabase(Group group){

        String addGroupSql = "INSERT INTO " + TABLE_GROUPS + " VALUES(null, '" + group.getGroupTitle() + "'); select last_insert_rowid()";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(addGroupSql, null);

        c.moveToFirst();
        int groupId = (int) c.getLong(0);

        linkUsersToGroup(group.getMembers(), groupId);
        linkExpensesToGroup(group.getExpenses(), groupId);

    }

}
