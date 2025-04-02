package com.example.goplay.model;


//https://github.com/belindaatschool/noteslist


import com.google.firebase.firestore.Exclude;

public class Venue {
    private String name;
    private String type;
    private double latitude;
    private double longtitude;
    private int capacity;
    private int playing;
    private String image;
    private String docId;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public Venue(String name, String type, double latitude, double longtitude, int capacity, String image) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.capacity = capacity;
        this.playing = 0;
        this.image = image;
    }

    public Venue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPlaying() {
        return playing;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
