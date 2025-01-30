package com.example.goplay.model;

//TODO
//https://github.com/belindaatschool/noteslist


public class Venue {
    private String venue_id;
    private String name;
    private String type;
    private long latitude;
    private long longtitude;
    private int capacity;
    private String availabilty_status;
    private String image;

    public Venue(String venue_id, String name, String type, long latitude, long longtitude, int capacity, String image) {
        this.venue_id = venue_id;
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

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
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

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(long longtitude) {
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
