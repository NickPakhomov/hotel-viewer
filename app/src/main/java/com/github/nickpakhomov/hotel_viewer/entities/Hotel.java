/*
package com.github.nickpakhomov.cp_test.entities;

import com.github.nickpakhomov.cp_test.models.ResponseBody;

import java.util.Locale;
import java.util.Objects;

*/
/**
 * Created by Nikolay Pakhomov on 08/09/17.
 *//*


public class Hotel {
    private int id;
    private String name;
    private String address;
    private float stars;
    private float distance;
    private String image;
    private String suites_availability;
    private float lat;
    private float lon;
    
    public Hotel(String name, String address, float stars, float distance, String suites_availability) {
        this.name = name;
        this.address = address;
        this.stars = stars;
        this.distance = distance;
        this.suites_availability = suites_availability;
    }
    
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
    
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        
        if (!(obj instanceof Hotel))
            return false;
        
        Hotel hotel2 = (Hotel) obj;
        
        return id == hotel2.id
                && name.equals(hotel2.name)
                && address.equals(hotel2.address)
                && stars == hotel2.stars
                && distance == hotel2.distance;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, stars, distance);
    }
    
    @Override
    public String toString() {
        return String.format(Locale.getDefault(),
                "Hotel - %s%nLocation: %s%nStars: %.1f",
                name,
                address,
                stars);
    }
    
    public static Hotel create(ResponseBody responseBody) {
        return new Hotel(responseBody.getName(), responseBody.getAddress(), responseBody.getDistance(), responseBody.get);
    }
}
*/
