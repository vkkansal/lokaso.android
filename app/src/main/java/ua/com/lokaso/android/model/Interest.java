package ua.com.lokaso.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.lokaso.android.constants.Constants;


public class Interest extends Application implements Constants {

    private String id;

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

    public String getInterest_pic() {
        return interest_pic;
    }

    public void setInterest_pic(String interest_pic) {
        this.interest_pic = interest_pic;
    }

    private String name;
    private String interest_pic;

    public Interest() {

    }

    public Interest(JSONObject jsonData) {

        try {

            if (jsonData != null) {
                this.setId(jsonData.getString("id"));
                this.setName(jsonData.getString("name"));
                this.setInterest_pic(jsonData.getString("interest_pic"));
            }

        } catch (Throwable t) {

            Log.e("Interest", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Interest", jsonData.toString());
        }
    }

}
