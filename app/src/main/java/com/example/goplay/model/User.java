package com.example.goplay.model;

public class User {
    private int user_id;
    private String email;
    private String password;
    private String name;
    private String sportsIntrest;
    private String location;


    public User() {
    }

    public User(int user_id, String email, String password, String name, String sportsIntrest, String location) {
        this.user_id = user_id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sportsIntrest = sportsIntrest;
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

