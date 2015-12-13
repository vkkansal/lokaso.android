package ua.com.lokaso.android;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.common.ActivityBase;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.Locations;
import ua.com.lokaso.android.tab.DashboardActivityTab;
import ua.com.lokaso.android.util.CustomRequest;
import ua.com.lokaso.android.util.Helper;


public class SignupActivity extends ActivityBase implements Constants, View.OnFocusChangeListener {

    private static final String TAG = "sirnup_activity";

    Toolbar toolbar;

    //CallbackManager callbackManager;

    //LoginButton loginButton;

    EditText signupFullname, signupPassword, signupEmail;
    ImageView signupImage;
    //AutoCompleteTextView signupLocation;
    TextView signupLocation;
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

        if (AccessToken.getCurrentAccessToken() != null) LoginManager.getInstance().logOut();

        //callbackManager = CallbackManager.Factory.create();
        Intent i = getIntent();
        facebookId = i.getStringExtra("facebookId");
        facebookName = i.getStringExtra("facebookName");
        facebookEmail = i.getStringExtra("facebookEmail");

        signupFullname = (EditText) findViewById(R.id.signupFullname);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        signupEmail = (EditText) findViewById(R.id.signupEmail);
        signupImage = (ImageView) findViewById(R.id.load_profile_pic);
        //signupLocation = (AutoCompleteTextView) findViewById(R.id.signupLocation);
        signupLocation = (TextView) findViewById(R.id.signupLocation);
        signupLocation.setOnClickListener(onClickListener);
        /*adapterLocations = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, locations);
        signupLocation.setAdapter(adapterLocations);*/
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

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupPassword.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkPassword();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        signupEmail.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                checkEmail();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
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
                                            } catch (JSONException e) {

                                            }

                                            switch (App.getInstance().getErrorCode()) {

                                                case NAME_ERROR: {

                                                    signupFullname.setError(errorStr);
                                                    break;
                                                }

                                                case EMAIL_ERROR: {

                                                    signupEmail.setError(errorStr);
                                                    break;
                                                }
                                                case PASSWORD_ERROR: {

                                                    signupPassword.setError(errorStr);
                                                    break;
                                                }
                                                case LOCATION_ERROR: {

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
                                if (locationsBean != null) {
                                    lat = locationsBean.getLat();
                                    log = locationsBean.getLng();
                                }
                                params.put("lat", lat);
                                params.put("lng", log);
                                if (facebookId != null && !facebookId.equalsIgnoreCase("")) {
                                    params.put("provider", "facebook");
                                } else {
                                    params.put("provider", "email");
                                }
                                params.put("email", email);
                                params.put("facebook_id", facebookId);
                                params.put("profile_pic_data", "");
                                Log.v("request params", String.valueOf(params.size()));
                                return params;
                            }
                        };
                        Log.v("request params jsonReq", String.valueOf(jsonReq));
                        App.getInstance().addToRequestQueue(jsonReq);

                    } else {

                        Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        LocalBroadcastManager.getInstance(SignupActivity.this).registerReceiver(mUpdateUIReceiver, new IntentFilter(Constants.IMAGE_RECEIVED));
        /*LoginManager.getInstance().registerCallback(callbackManager,
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

                                                if (AccessToken.getCurrentAccessToken() != null)
                                                    LoginManager.getInstance().logOut();

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
                });*/
        /*if (App.getInstance().isConnected()) {
            getLocationsStream();
        }*/
    }

    public Map<String, String> getRequestParametersMap() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", fullname);
        params.put("pass", password);
        Locations locationsBean = locationsMap.get(location);
        String lat = "NA";
        String log = "NA";
        if (locationsBean != null) {
            lat = locationsBean.getLat();
            log = locationsBean.getLng();
        }
        params.put("lat", lat);
        params.put("lng", log);
        if (facebookId != null && !facebookId.equalsIgnoreCase("")) {
            params.put("provider", "facebook");
        } else {
            params.put("provider", "email");
        }
        params.put("email", email);
        params.put("facebook_id", facebookId);
        params.put("profile_pic_data", "");
        Log.v("request params", String.valueOf(params.size()));
        return params;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

            switch(requestCode)
            {

                case Constants.CAMERA_PIC_REQUEST:
                    try
                    {
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        //storeBitmapImage(thumbnail);
                        Intent intent = new Intent(Constants.IMAGE_RECEIVED);
                        intent.putExtra("image", thumbnail);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    }
                    catch(Exception ex)
                    {
                        Helper.showAlertResponse(SignupActivity.this, "Image not received. Please try again!", false, false);
                        Log.e(this.getClass().getName(), "Amit error["+ex.getMessage()+"]");
                    }
                    break;
                case Constants.SELECT_FILE1:
                    Log.v(this.getClass().getName(),"Amit image gallary["+data+"]" );
                    try {
                        InputStream stream = getContentResolver().openInputStream(data.getData());

                        Bitmap bitmap = decodeSampledBitmapFromResourceMemOpt(stream, 100,
                                100);//BitmapFactory.decodeStream(stream);
                        stream.close();

                        Intent intent1 = new Intent(Constants.IMAGE_RECEIVED);
                        intent1.putExtra("image", bitmap);
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                    } catch (Exception ex){
                        Log.e(this.getClass().getName(), "File not found ["+ex.getMessage()+"]");
                        Helper.showAlertResponse(SignupActivity.this, "Unable to fetch the image. Please try again!", false, false);
                    }
                    break;
                case Constants.LOCATION_SELECTION:
                    if (resultCode == RESULT_OK) {
                        Place place = PlacePicker.getPlace(data, this);
                        String toastMsg = String.format("Place: %s", place.getName());
                        Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                        Log.e(this.getClass().getName(), "Location [" + toastMsg + "]");
                    }
                    break;
                default:
                    Log.v("SignupActivity", "Amit galery ["+data+"]");
            }


    }
    public Bitmap decodeSampledBitmapFromResourceMemOpt(InputStream inputStream, int reqWidth, int reqHeight) {

        byte[] byteArr = new byte[0];
        byte[] buffer = new byte[1024];
        int len;
        int count = 0;

        try {
            while ((len = inputStream.read(buffer)) > -1) {
                if (len != 0) {
                    if (count + len > byteArr.length) {
                        byte[] newbuf = new byte[(count + len) * 2];
                        System.arraycopy(byteArr, 0, newbuf, 0, count);
                        byteArr = newbuf;
                    }

                    System.arraycopy(buffer, 0, byteArr, count, len);
                    count += len;
                }
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArr, 0, count, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth,	reqHeight);
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            int[] pids = { android.os.Process.myPid() };
            //MemoryInfo myMemInfo = mAM.getProcessMemoryInfo(pids)[0];
            //Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss);

            return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }


        return inSampleSize;

    }

    public void signinByFacebookId() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_AUTH_FACEBOOK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (App.getInstance().authorize(response)) {

                            if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

                                ActivityCompat.finishAffinity(SignupActivity.this);

                                Intent i = new Intent(getApplicationContext(), DashboardActivityTab.class);
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
    public void onBackPressed() {

        finish();
    }

    //ImageView mypic;

    public void imageCaptureOptions(View v) {
      //  mypic = (ImageView) v;
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignupActivity.this);

        builderSingle.setTitle("Select a profile picture");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignupActivity.this,
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
                        if ("Take photo".equalsIgnoreCase(strName)) {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, Constants.CAMERA_PIC_REQUEST);
                        } else if ("Choose from library".equalsIgnoreCase(strName)) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select file to upload "), Constants.SELECT_FILE1);
                        }

                    }
                });
        builderSingle.show();

    }


//=========================================================================

    private BroadcastReceiver mUpdateUIReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            int w = signupImage.getWidth();
            int h = signupImage.getHeight();
            signupImage.setImageBitmap((Bitmap) b.get("image"));
            signupImage.setLayoutParams(new LinearLayout.LayoutParams(w, h));
            //bundle.putAll(b); // Might be erroneous;

        }
    };
//=========================================================================


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
                                        Log.v("answerObj", answerObj.toString());
                                        //COUNTRIES[i] = answerObj.getString("name");
                                        Locations locationsBean = new Locations(answerObj);
                                        locations.add(answerObj.getString("name"));
                                        locationsMap.put(answerObj.getString("name"), locationsBean);
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int PLACE_PICKER_REQUEST = Constants.LOCATION_SELECTION;
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

            try {
                startActivityForResult(builder.build(SignupActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    };
}
