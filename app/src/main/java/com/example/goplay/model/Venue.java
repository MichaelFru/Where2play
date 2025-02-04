package com.example.goplay.model;

//TODO
//https://github.com/belindaatschool/noteslist


public class Venue {
    private String name;
    private String type;
    private double latitude;
    private double longtitude;
    private int capacity;
    private String availabilty_status;
    private String image;

    public Venue( String name, String type, double latitude, double longtitude, int capacity, String image) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.capacity = capacity;
        this.availabilty_status = "available";
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

    public String getAvailabilty_status() {
        return availabilty_status;
    }

    public void setAvailabilty_status(String availabilty_status) {
        this.availabilty_status = availabilty_status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
