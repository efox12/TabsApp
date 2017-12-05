package com.foxbrajcich.tabs;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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

    public static void setName(String name){UserSession.name = name;}

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

}
