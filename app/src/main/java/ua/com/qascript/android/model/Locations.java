package ua.com.qascript.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.qascript.android.constants.Constants;


public class Locations extends Application implements Constants {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    private String name;
    private String state;

    private String lat, lng, country;

    public Locations() {

    }

    public Locations(JSONObject jsonData) {

        try {

            if (jsonData != null) {
                this.setId(jsonData.getString("id"));
                this.setName(jsonData.getString("name"));
                this.setState(jsonData.getString("state"));
                this.setLat(jsonData.getString("lat"));
                this.setLng(jsonData.getString("lng"));
                this.setCountry(jsonData.getString("country"));
            }

        } catch (Throwable t) {

            Log.e("UserFriends", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("UserFriends", jsonData.toString());
        }
    }

}
