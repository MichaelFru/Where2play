package com.example.goplay.database_classes;

public class User {
    private int user_id;
    private String name;
    private String sportsIntrest;
    private String location;


    public User() {
    }

    public User(int user_id, String name, String sportsIntrest, String location) {
        this.user_id = user_id;
        this.name = name;
        this.sportsIntrest = sportsIntrest;
        this.location = location;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public String getSportsIntrest() {
        return sportsIntrest;
    }

    public void setSportsIntrest(String sportsIntrest) {
        this.sportsIntrest = sportsIntrest;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

