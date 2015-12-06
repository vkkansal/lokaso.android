package ua.com.qascript.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.qascript.android.constants.Constants;

/**
 * Created by Администратор on 15.03.2015.
 */
public class Notify extends Application implements Constants {

    private long id, answerId, fromUserId;
    private int fromUserState, createAt, type;
    private String fromUserUsername, fromUserFullname, fromUserPhotoUrl;

    public Notify() {

    }

    public Notify(JSONObject jsonData) {

        try {

            this.setId(jsonData.getLong("id"));
            this.setType(jsonData.getInt("type"));
            this.setAnswerId(jsonData.getLong("answerId"));
            this.setFromUserId(jsonData.getLong("fromUserId"));
            this.setFromUserState(jsonData.getInt("fromUserState"));
            this.setFromUserUsername(jsonData.getString("fromUserUsername"));
            this.setFromUserFullname(jsonData.getString("fromUserFullname"));
            this.setFromUserPhotoUrl(jsonData.getString("fromUserPhotoUrl"));
            this.setCreateAt(jsonData.getInt("createAt"));

        } catch (Throwable t) {

            Log.e("Notify", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Notify", jsonData.toString());
        }
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getId() {

        return this.id;
    }

    public void setType(int type) {

        this.type = type;
    }

    public int getType() {

        return this.type;
    }

    public void setAnswerId(long answerId) {

        this.answerId = answerId;
    }

    public long getAnswerId() {

        return this.answerId;
    }

    public void setFromUserId(long fromUserId) {

        this.fromUserId = fromUserId;
    }

    public long getFromUserId() {

        return this.fromUserId;
    }

    public void setFromUserState(int fromUserState) {

        this.fromUserState = fromUserState;
    }

    public int getFromUserState() {

        return this.fromUserState;
    }

    public void setFromUserUsername(String fromUserUsername) {

        this.fromUserUsername = fromUserUsername;
    }

    public String getFromUserUsername() {

        return this.fromUserUsername;
    }

    public void setFromUserFullname(String fromUserFullname) {

        this.fromUserFullname = fromUserFullname;
    }

    public String getFromUserFullname() {

        return this.fromUserFullname;
    }

    public void setFromUserPhotoUrl(String fromUserPhotoUrl) {

        this.fromUserPhotoUrl = fromUserPhotoUrl;
    }

    public String getFromUserPhotoUrl() {

        return this.fromUserPhotoUrl;
    }

    public void setCreateAt(int createAt) {

        this.createAt = createAt;
    }

    public int getCreateAt() {

        return this.createAt;
    }
}
