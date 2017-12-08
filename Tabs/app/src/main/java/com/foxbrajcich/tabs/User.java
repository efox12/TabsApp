package com.foxbrajcich.tabs;

import java.io.Serializable;

/**
 * Created by erikfox on 11/15/17.
 */

public class User implements Serializable{
    private String name;
    private String username;
    private boolean isOnline;

    public User(){
        this.name = "";
        this.username = "";
    }

    public User(String name){
        this.name = name;
        this.username = "";
    }

    public User(String name, String username){
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}