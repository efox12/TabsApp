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
    private static List<String> friendsUsernames;

    static {
        friendsUsernames = new ArrayList<>();
    }

    private UserSession(){}

    public static void clearUserSession(){
        username = null;
        name = null;
        friendsUsernames = new ArrayList<>();
    }

    public static void setName(String name){UserSession.name = name;}

    public static String getName(){
        return name;
    }

    public static void setUsername(String username){UserSession.username = username;}

    public static String getUsername(){
        return username;
    }

    public static List<String> getFriendsUsernames(){
        return friendsUsernames;
    }

    public static void addFriendsUsername(String username){
        friendsUsernames.add(username);
    }

}
