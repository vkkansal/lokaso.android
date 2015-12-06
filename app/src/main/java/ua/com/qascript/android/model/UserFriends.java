package ua.com.qascript.android.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.util.CustomRequest;


public class UserFriends extends Application implements Constants {

    private String user_id, name, email;

    public String[] getInterests() {
        return interests;
    }

    public void setInterests(String[] interests) {
        this.interests = interests;
    }

    private String[] interests;

    private String lat, lng, user_pic, num_asks, num_responses, create_date, distance;



    public UserFriends() {

    }

    public UserFriends(JSONObject jsonData) {

        try {
            String[] interestArray = new String[]{"0"};
            if (jsonData != null) {
                this.setId(jsonData.getString("user_id"));
                this.setName(jsonData.getString("name"));
                this.setEmail(jsonData.getString("email"));
                this.setLat(jsonData.getString("lat"));
                this.setLng(jsonData.getString("lng"));
                this.setUser_pic(jsonData.getString("user_pic"));
                this.setNum_asks(jsonData.getString("num_asks"));
                this.setNum_responses(jsonData.getString("num_responses"));
                this.setDistance(jsonData.getString("distance"));//jsonData.getString("distance"));
                /*if(jsonData.getString("interests").contains(",")){
                    interestArray = jsonData.getString("interests").split(",");
                }else if(!jsonData.getString("interests").equalsIgnoreCase("")){
                    interestArray = new String[1];
                    interestArray[0] = jsonData.getString("interests");
                }
                this.setInterests(interestArray);*/
                this.setCreate_date(jsonData.getString("create_date"));
            }

        } catch (Throwable t) {

            Log.e("UserFriends", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("UserFriends", jsonData.toString());
        }
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
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

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
