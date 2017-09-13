package com.github.nickpakhomov.hotel_viewer.models;

/**
 * Holder object for hotel values
 * Created by Nikolay Pakhomov on 03/05/17.
 */

@SuppressWarnings("CanBeFinal, unused")
public class ResponseBody {
    private int id;
    private String name;
    private String address;
    private float stars;
    private float distance;
    private String image;
    private String suites_availability;
    private float lat;
    private float lon;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public float getStars() {
        return stars;
    }
    
    public float getDistance() {
        return distance;
    }
    
    public String getImage() {
        return image;
    }
    
    public String getSuitesAvailability() {
        return suites_availability;
    }
    
    public float getLat() {
        return lat;
    }
    
    public float getLon() {
        return lon;
    }
}
