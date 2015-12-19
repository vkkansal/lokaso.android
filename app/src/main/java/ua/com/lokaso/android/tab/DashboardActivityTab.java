package ua.com.lokaso.android.tab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserAsks;
import ua.com.lokaso.android.model.UserFriends;
import ua.com.lokaso.android.service.GPSTracker;
import ua.com.lokaso.android.util.CustomRequest;
import ua.com.lokaso.android.view.ProfileActivityNew;

public class DashboardActivityTab extends AppCompatActivity implements Constants{
    int offset = 0;
    int limit = 1;
    int pageNo = 0;
    int arrayLength = 0;
    Toolbar toolbar;
    ImageButton imageButton;
    PagerAdapterDashboard adapter;
    RelativeLayout mProfileLoadingScreen;
    GPSTracker gps;
    TabLayout tabLayout;
    private List<UserFriends> streamListFollowing = new ArrayList<UserFriends>();
    private List<UserAsks> streamListAsks = new ArrayList<UserAsks>();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_tab);

        }

    private void checkLocation(){
        gps = new GPSTracker(DashboardActivityTab.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            App.getInstance().setLat(String.valueOf(latitude));
            App.getInstance().setLng(String.valueOf(longitude));
            init();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

        private void init(){
            imageButton = (ImageButton)findViewById(R.id.imageButtonProfile);
            mProfileLoadingScreen = (RelativeLayout) findViewById(R.id.profileLoadingScreen);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileButtonHandler(v);
                }
            });
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Around You");
            toolbar.setTitleTextColor(Color.WHITE);
            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.removeAllTabs();
            tabLayout.addTab(tabLayout.newTab().setText("Folks"));
            tabLayout.addTab(tabLayout.newTab().setText("Asks"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setTabTextColors(getResources().getColor(R.color.tab_unselected_text), getResources().getColor(R.color.tab_selected_text));
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            adapter = new PagerAdapterDashboard
                    (getSupportFragmentManager(), tabLayout.getTabCount(), streamListFollowing, streamListAsks);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            getStreamAsks();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                //getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                        return true;
                }

                return super.onOptionsItemSelected(item);
        }

        public void profileButtonHandler(View v) {
                Intent i = new Intent(DashboardActivityTab.this, ProfileActivityNew.class);
                i.putExtra("profileId", String.valueOf(App.getInstance().getId()));
                startActivity(i);
        }

    public void showLoadingScreen() {

        mProfileLoadingScreen.setVisibility(View.VISIBLE);

    }

    public void removeLoadingScreen() {

        mProfileLoadingScreen.setVisibility(View.GONE);

    }

    public void getStreamFolks() {
        long profileId = App.getInstance().getId();
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_FOLKS_GET+profileId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            arrayLength = 0;
                            if (response != null) {
                                streamListFollowing.clear();
                                JSONArray usersArray = response.getJSONArray("users");

                                arrayLength = usersArray.length();
                                if (arrayLength > 0) {

                                    for (int i = 0; i < usersArray.length(); i++) {

                                        JSONObject answerObj = (JSONObject) usersArray.get(i);
                                        UserFriends answer = new UserFriends(answerObj);

                                        streamListFollowing.add(answer);
                                    }
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            removeLoadingScreen();
                            adapter.notifyDataSetChanged();
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
                params.put("offset", String.valueOf(offset));
                params.put("limit", String.valueOf(limit));
                offset = offset +limit;
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void getStreamAsks() {
        showLoadingScreen();
        long profileId = App.getInstance().getId();
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_ASKS_GET+profileId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            arrayLength = 0;
                            if (response != null) {
                                streamListAsks.clear();
                                JSONArray usersArray = response.getJSONArray("users");

                                arrayLength = usersArray.length();
                                if (arrayLength > 0) {

                                    for (int i = 0; i < usersArray.length(); i++) {

                                        JSONObject answerObj = (JSONObject) usersArray.get(i);

                                        UserAsks answer = new UserAsks(answerObj);

                                        streamListAsks.add(answer);
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            getStreamFolks();
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
                params.put("offset", String.valueOf(offset));
                params.put("limit", String.valueOf(limit));
                offset = offset +10;
                limit = limit +10;
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocation();
    }
}
