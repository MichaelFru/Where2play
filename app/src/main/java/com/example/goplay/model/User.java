package com.example.goplay.model;

import com.google.firebase.firestore.Exclude;

public class User {
    @Exclude
    private int user_id;


    private String name;
    private Venue currentVenue;

    public User() {
    }

    public User(String name) {
        this.name = name;
        this.currentVenue = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Venue getCurrentVenue() {
        return currentVenue;
    }

    public void setCurrentVenue(Venue currentVenue) {
        this.currentVenue = currentVenue;
    }
}
