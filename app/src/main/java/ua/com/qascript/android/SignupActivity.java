package ua.com.qascript.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ua.com.qascript.android.app.App;
import ua.com.qascript.android.common.ActivityBase;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.model.Locations;
import ua.com.qascript.android.tab.MainActivityTab;
import ua.com.qascript.android.util.CustomRequest;
import ua.com.qascript.android.util.Helper;


public class SignupActivity extends ActivityBase implements Constants, View.OnFocusChangeListener {

    private static final String TAG = "sirnup_activity";

    Toolbar toolbar;

    CallbackManager callbackManager;

    //LoginButton loginButton;

    EditText signupFullname, signupPassword, signupEmail;
    AutoCompleteTextView signupLocation;
    Button signupJoinHowBtn;
    TextView mRegularSignup, mLabelAuthorizationViaFacebook;
    private Spinner signupInterest;
    private String username, fullname, password, email, language, location;
    String facebookId = "", facebookName = "", facebookEmail = "";
    private static String[] COUNTRIES = new String[0];
    private static List<String> locations = new ArrayList<String>();
    Map<String, String> requestParams = new HashMap<String, String>();
    private static Map<String, Locations> locationsMap = new HashMap<String, Locations>();

    ArrayAdapter<String> adapterLocations = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);//_new);

        if (AccessToken.getCurrentAccessToken()!= null) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        Intent i = getIntent();
        facebookId = i.getStringExtra("facebookId");
        facebookName = i.getStringExtra("facebookName");
        facebookEmail = i.getStringExtra("facebookEmail");

        signupFullname = (EditText) findViewById(R.id.signupFullname);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupEmail = (EditText) findViewById(R.id.signupEmail);
        signupLocation = (AutoCompleteTextView) findViewById(R.id.signupLocation);
        adapterLocations = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        signupLocation.setAdapter(adapterLocations);
        //addItemsOnSpinner();
        signupFullname.setText(facebookName);
        signupEmail.setText(facebookEmail);

        if (facebookId != null && !facebookId.equals("")) {

            //loginButton.setVisibility(View.GONE);
           // mRegularSignup.setVisibility(View.VISIBLE);
            //mLabelAuthorizationViaFacebook.setVisibility(View.VISIBLE);
        }

        if (facebookId == null) {

            facebookId = "";
        }

        //signupUsername.setOnFocusChangeListener(this);
        signupFullname.setOnFocusChangeListener(this);
        signupPassword.setOnFocusChangeListener(this);
        signupEmail.setOnFocusChangeListener(this);

        signupFullname.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkFullname();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        signupPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkPassword();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        signupEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkEmail();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        signupJoinHowBtn = (Button) findViewById(R.id.signupJoinHowBtn);

        signupJoinHowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = signupFullname.getText().toString();
                password = signupPassword.getText().toString();
                location = signupLocation.getText().toString();
                //interest = signupInterest.getSelectedItem().toString();
                email = signupEmail.getText().toString();
                language = Locale.getDefault().getLanguage();

                if (verifyRegForm()) {

                    if (App.getInstance().isConnected()) {

                        showpDialog();
                        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_AUTH_SIGNUP_LOCAL, getRequestParametersMap(),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");

                                        if (App.getInstance().authorize(response)) {

                                            Log.e("Profile", "Malformed JSON: \"" + response.toString() + "\"");

                                            ActivityCompat.finishAffinity(SignupActivity.this);

                                            Intent i = new Intent(getApplicationContext(), InterestListView.class);
                                            startActivity(i);

                                        } else {
                                            String errorStr = "Error";
                                            try {
                                                errorStr = response.getString("error");
                                            }catch(JSONException e){

                                            }

                                            switch (App.getInstance().getErrorCode()) {

                                                case NAME_ERROR : {

                                                    signupFullname.setError(errorStr);
                                                    break;
                                                }

                                                case EMAIL_ERROR : {

                                                    signupEmail.setError(errorStr);
                                                    break;
                                                }
                                                case PASSWORD_ERROR : {

                                                    signupPassword.setError(errorStr);
                                                    break;
                                                }
                                                case LOCATION_ERROR : {

                                                    signupLocation.setError(errorStr);
                                                    break;
                                                }

                                                default: {

                                                    Log.e("Profile", "Could not parse malformed JSON: \"" + response.toString() + "\"");
                                                    break;
                                                }
                                            }
                                        }

                                        hidepDialog();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                                hidepDialog();
                            }
                        }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", fullname);
                                params.put("pass", password);
                                Locations locationsBean = locationsMap.get(location);
                                String lat = "NA";
                                String log = "NA";
                                if(locationsBean != null){
                                    lat = locationsBean.getLat();
                                    log = locationsBean.getLng();
                                }
                                params.put("lat", lat);
                                params.put("lng", log);
                                if(facebookId != null && !facebookId.equalsIgnoreCase("")){
                                    params.put("provider", "facebook");
                                }else{
                                    params.put("provider", "email");
                                }
                                params.put("email", email);
                                params.put("facebook_id", facebookId);
                                params.put("profile_pic_data", "");
                                Log.v("request params",String.valueOf(params.size()));
                               return params;
                            }
                        };
                        Log.v("request params jsonReq",String.valueOf(jsonReq));
                        App.getInstance().addToRequestQueue(jsonReq);

                    } else {

                        Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        // App code

                        if (App.getInstance().isConnected()) {

                            showpDialog();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {

                                            // Application code

                                            try {

                                                facebookId = object.getString("id");
                                                facebookName = object.getString("name");

                                                if (object.has("email")) {

                                                    facebookEmail = object.getString("email");
                                                }

                                            } catch (Throwable t) {

                                                Log.e("Profile", "Could not parse malformed JSON: \"" + object.toString() + "\"");

                                            } finally {

                                                if (AccessToken.getCurrentAccessToken() != null) LoginManager.getInstance().logOut();

                                                Log.d("Profile", object.toString());

                                                if (App.getInstance().isConnected()) {

                                                    if (!facebookId.equals("")) {

                                                        signinByFacebookId();

                                                    } else {

                                                        hidepDialog();
                                                    }

                                                } else {

                                                    hidepDialog();
                                                }
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }
                    }

                    @Override
                    public void onCancel() {

                        // App code
                        // Cancel
                    }

                    @Override
                    public void onError(FacebookException exception) {

                        // App code
                        // Error
                    }
                });
        if (App.getInstance().isConnected()) {
            getLocationsStream();
        }
    }

    public Map<String, String> getRequestParametersMap(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", fullname);
        params.put("pass", password);
        Locations locationsBean = locationsMap.get(location);
        String lat = "NA";
        String log = "NA";
        if(locationsBean != null){
            lat = locationsBean.getLat();
            log = locationsBean.getLng();
        }
        params.put("lat", lat);
        params.put("lng", log);
        if(facebookId != null && !facebookId.equalsIgnoreCase("")){
            params.put("provider", "facebook");
        }else{
            params.put("provider", "email");
        }
        params.put("email", email);
        params.put("facebook_id", facebookId);
        params.put("profile_pic_data", "");
        Log.v("request params",String.valueOf(params.size()));
        return params;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void signinByFacebookId() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_AUTH_FACEBOOK,  null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (App.getInstance().authorize(response)) {

                            if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                ActivityCompat.finishAffinity(SignupActivity.this);

                                Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
                                startActivity(i);

                            } else {

                                if (App.getInstance().getState() == ACCOUNT_STATE_BLOCKED) {

                                    App.getInstance().logout();
                                    Toast.makeText(SignupActivity.this, getText(R.string.account_blocked), Toast.LENGTH_SHORT).show();

                                } else {

                                    ActivityCompat.finishAffinity(SignupActivity.this);

                                    Intent i = new Intent(getApplicationContext(), InterestListView.class);
                                    startActivity(i);
                                }
                            }

                        } else {

                            if (facebookId != "") {

                                //.setVisibility(View.GONE);
                                //mRegularSignup.setVisibility(View.VISIBLE);
                                //mLabelAuthorizationViaFacebook.setVisibility(View.VISIBLE);

                                signupFullname.setText(facebookName);

                                if (facebookEmail != null && !facebookEmail.equals("")) {

                                    signupEmail.setText(facebookEmail);
                                }
                            }
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

                hidepDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("facebookId", facebookId);
                params.put("clientId", CLIENT_ID);
                params.put("gcm_regId", App.getInstance().getGcmToken());

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {

            /*case R.id.signupUsername: {

                break;
            }*/

            case R.id.signupFullname: {

                if (!hasFocus) {


                }

                break;
            }

            case R.id.signupPassword: {

                if (!hasFocus) {


                }

                break;
            }

            case R.id.signupEmail: {

                if (!hasFocus) {


                }

                break;
            }

            default: {

                break;
            }
        }
    }

    public Boolean checkFullname() {

        fullname = signupFullname.getText().toString();

        fullname = fullname.trim();

        if (fullname.length() == 0) {

            signupFullname.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            signupFullname.setError(getString(R.string.error_small_fullname));

            return false;
        }

        signupFullname.setError(null);

        return true;
    }

    public Boolean checkPassword() {

        password = signupPassword.getText().toString();

        Helper helper = new Helper();

        if (password.length() == 0) {

            signupPassword.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            signupPassword.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            signupPassword.setError(getString(R.string.error_wrong_format));

            return false;
        }

        signupPassword.setError(null);

        return true;
    }

    public Boolean checkEmail() {

        email = signupEmail.getText().toString();

        Helper helper = new Helper();

        if (email.length() == 0) {

            signupEmail.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            signupEmail.setError(getString(R.string.error_wrong_format));

            return false;
        }

        signupEmail.setError(null);

        return true;
    }

    public Boolean verifyRegForm() {

        //signupUsername.setError(null);
        signupFullname.setError(null);
        signupPassword.setError(null);
        signupEmail.setError(null);
        signupLocation.setError(null);
        Helper helper = new Helper();

        if (location.length() == 0) {

            signupLocation.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() == 0) {

            signupFullname.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (fullname.length() < 2) {

            signupFullname.setError(getString(R.string.error_small_fullname));

            return false;
        }

        if (password.length() == 0) {

            signupPassword.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (password.length() < 6) {

            signupPassword.setError(getString(R.string.error_small_password));

            return false;
        }

        if (!helper.isValidPassword(password)) {

            signupPassword.setError(getString(R.string.error_wrong_format));

            return false;
        }

        if (email.length() == 0) {

            signupEmail.setError(getString(R.string.error_field_empty));

            return false;
        }

        if (!helper.isValidEmail(email)) {

            signupEmail.setError(getString(R.string.error_wrong_format));

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed(){

        finish();
    }

    public void imageCaptureOptions(View v){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignupActivity.this);

        builderSingle.setTitle("Select a profile picture");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>( SignupActivity.this,
                android.R.layout.select_dialog_singlechoice);

        arrayAdapter.add("Import from facebook");
        arrayAdapter.add("Take photo");
        arrayAdapter.add("Choose from library");
        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                    }
                });
        builderSingle.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    /*public void addItemsOnSpinner() {

        signupInterest = (Spinner) findViewById(R.id.signupInterest);
        List<String> list = new ArrayList<String>();
        list.add("Area of Interest");
        list.add("Food");
        list.add("Culture");
        list.add("Adventure");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        signupInterest.setAdapter(dataAdapter);
    }*/

    int arrayLength = 0;

    public void getLocationsStream() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_LOCATIONS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            arrayLength = 0;
                            if (response != null) {

                                JSONArray usersArray = response.getJSONArray("locations");

                                arrayLength = usersArray.length();

                                //COUNTRIES = new String[arrayLength];

                                if (arrayLength > 0) {

                                    for (int i = 0; i < usersArray.length(); i++) {

                                        JSONObject answerObj = (JSONObject) usersArray.get(i);
                                        Log.v("answerObj",answerObj.toString());
                                        //COUNTRIES[i] = answerObj.getString("name");
                                        Locations locationsBean = new Locations(answerObj);
                                        locations.add(answerObj.getString("name"));
                                        locationsMap.put(answerObj.getString("name"),locationsBean);
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            adapterLocations.notifyDataSetChanged();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }
}
