package com.foxbrajcich.tabs;

/**
 * Created by Robert on 11/28/2017.
 */

public class UserSession {

    private static String username;
    private static String name;
    private static int userId;

    private UserSession(){}

    public static void setName(String name){
        UserSession.name = name;
    }

    public static String getName(){
        return name;
    }

    public static void setUsername(String username){
        UserSession.username = name;
    }

    public static String getUsername(){
        return username;
    }

}
