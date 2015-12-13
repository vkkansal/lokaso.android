package ua.com.lokaso.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.lokaso.android.constants.Constants;


public class UserPhotos extends Application implements Constants {

    private String title, description;

    private String pic, create_date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public UserPhotos(JSONObject jsonData) {

        try {
            if (jsonData != null) {
                this.setTitle(jsonData.getString("title"));
                this.setDescription(jsonData.getString("description"));
                this.setPic(jsonData.getString("pic"));
                this.setCreate_date(jsonData.getString("create_date"));
            }
        } catch (Throwable t) {

            Log.e("UserPhotos", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("UserPhotos", jsonData.toString());
        }
    }

}
