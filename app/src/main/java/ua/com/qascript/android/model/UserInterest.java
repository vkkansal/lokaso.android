package ua.com.qascript.android.model;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import ua.com.qascript.android.constants.Constants;


public class UserInterest extends Application implements Constants {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String iduser;
    private String idinterest;

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdinterest() {
        return idinterest;
    }

    public void setIdinterest(String idinterest) {
        this.idinterest = idinterest;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    private String create_date;

    public UserInterest() {

    }

    public UserInterest(JSONObject jsonData) {

        try {
            if (jsonData != null) {
                this.setId(jsonData.getString("id"));
                this.setIdinterest(jsonData.getString("idinterest"));
                this.setIduser(jsonData.getString("iduser"));
                this.setCreate_date(jsonData.getString("create_date"));
            }
        } catch (Throwable t) {

            Log.e("Interest", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Interest", jsonData.toString());
        }
    }

}
