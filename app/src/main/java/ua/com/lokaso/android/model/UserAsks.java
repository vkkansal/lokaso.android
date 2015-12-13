package ua.com.lokaso.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.lokaso.android.constants.Constants;


public class UserAsks extends Application implements Constants {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAsk_title() {
        return ask_title;
    }

    public void setAsk_title(String ask_title) {
        this.ask_title = ask_title;
    }

    public String getAsk_desc() {
        return ask_desc;
    }

    public void setAsk_desc(String ask_desc) {
        this.ask_desc = ask_desc;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getUser_pic() {
        return pic;
    }

    public void setUser_pic(String user_pic) {
        this.pic = user_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCnt_response() {
        return cnt_response;
    }

    public void setCnt_response(String cnt_response) {
        this.cnt_response = cnt_response;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    private String id, name, email, ask_title, ask_desc, distance;

    private String lat, lng, pic, status, cnt_response, create_date;

    public UserAsks() {

    }

    public UserAsks(JSONObject jsonData) {

        try {

            if (jsonData != null) {
                this.setId(jsonData.getString("id"));
                this.setName(jsonData.getString("name"));
                this.setEmail(jsonData.getString("email"));
                this.setLat(jsonData.getString("lat"));
                this.setLng(jsonData.getString("lng"));
                this.setUser_pic(jsonData.getString("pic"));
                this.setAsk_title(jsonData.getString("ask_title"));
                this.setAsk_desc(jsonData.getString("ask_desc"));
                this.setCreate_date(jsonData.getString("create_date"));
                this.setStatus(jsonData.getString("status"));
                this.setDistance(jsonData.getString("distance"));
                this.setCnt_response(jsonData.getString("cnt_response"));
            }

        } catch (Throwable t) {

            Log.e("UserAsks", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("UserAsks", jsonData.toString());
        }
    }

}
