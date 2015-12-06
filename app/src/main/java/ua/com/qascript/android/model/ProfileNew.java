package ua.com.qascript.android.model;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.util.CustomRequest;

/**
 * Created by Administrator on 22.02.2015.
 */
public class ProfileNew extends Application implements Constants {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNum_asks() {
        return num_asks;
    }

    public void setNum_asks(String num_asks) {
        this.num_asks = num_asks;
    }

    public String getNum_responses() {
        return num_responses;
    }

    public void setNum_responses(String num_responses) {
        this.num_responses = num_responses;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    private long id;

    private String num_responses, provider, create_date;

    private String name, email, phone, password, lat, lng, user_pic, num_asks;

    public ProfileNew() {

    }

    public ProfileNew(JSONObject jsonData, long id) {

        try {

            if (jsonData.getBoolean("error") == false) {

                this.setId(jsonData.getLong("id"));
                this.setName(jsonData.getString("name"));
                this.setEmail(jsonData.getString("email"));
                this.setPhone(jsonData.getString("phone"));
                this.setPassword(jsonData.getString("password"));
                this.setLat(jsonData.getString("lat"));
                this.setLng(jsonData.getString("lng"));
                this.setUser_pic(jsonData.getString("user_pic"));
                this.setNum_asks(jsonData.getString("num_asks"));
                this.setNum_responses(jsonData.getString("num_responses"));
                this.setProvider(jsonData.getString("provider"));
                this.setCreate_date(jsonData.getString("create_date"));

            }

        } catch (Throwable t) {

            Log.e("Profile", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Profile", jsonData.toString());
        }
    }
}
