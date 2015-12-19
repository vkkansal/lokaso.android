package ua.com.lokaso.android.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.UserPicUpload;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserAsks;
import ua.com.lokaso.android.model.UserFriends;
import ua.com.lokaso.android.model.UserPhotos;
import ua.com.lokaso.android.tab.PagerAdapterProfile;
import ua.com.lokaso.android.util.CustomRequest;

public class ProfileActivityNew extends AppCompatActivity implements Constants{
    public String profile_id = "1";
    Toolbar toolbar;
    PagerAdapterProfile adapter;
    private List<UserPhotos> streamListUserPhotos = new ArrayList<UserPhotos>();
    private List<UserFriends> streamListFollowing = new ArrayList<UserFriends>();
    private List<UserAsks> streamListAsks = new ArrayList<UserAsks>();
    String profileName = "My Profile";
    TextView userPhotos, userAsks, userFollowers;
    ImageView imageView;
    Button statusButton;
    RelativeLayout mProfileLoadingScreen;
    ViewPager viewPager;
    ImageLoader imageLoader = App.getInstance().getImageLoader();
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page_new);
        userPhotos = (TextView)findViewById(R.id.postTxt_tv);
        userAsks = (TextView)findViewById(R.id.askTxt_tv);
        userFollowers = (TextView)findViewById(R.id.followingTxt_tv);
        imageView = (ImageView)findViewById(R.id.profile_image_id);
        statusButton = (Button)findViewById(R.id.status);
        mProfileLoadingScreen = (RelativeLayout) findViewById(R.id.profileLoadingScreen);
        //Initialize Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(profileName);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        try {
            profile_id = i.getStringExtra("profileId");
        }
        catch(Exception e){

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Photos"));
        tabLayout.addTab(tabLayout.newTab().setText("Asks"));
        tabLayout.addTab(tabLayout.newTab().setText("Following"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.tab_unselected_text), getResources().getColor(R.color.tab_selected_text));
        viewPager = (ViewPager) findViewById(R.id.profile_detail_contents_viewpager);

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


    }
    int arrayLength = 0;
    /*public void getDataStatic() {
        try {
            JSONObject response = new JSONObject("{\"user_data\":[{\"name\":\"Vivek Gupta\",\"email\":\"vk@xperpendicular.com\",\"phone\":\"+919764443539\",\"password\":\"p\",\"lat\":\"18.97500000\",\"lng\":\"72.82580000\",\"user_pic\":\"http:\\/\\/xperpendicular.com\\/lokaso\\/user_pics\\/vivek.jpg\",\"num_asks\":\"7\",\"num_responses\":\"101\",\"provider\":\"LOCASO\",\"create_date\":\"2015-11-22 11:05:43\"}],\"user_interests\":[{\"id\":\"1\",\"iduser\":\"1\",\"idinterest\":\"1\",\"create_date\":\"2015-11-22 11:05:43\"},{\"id\":\"2\",\"iduser\":\"1\",\"idinterest\":\"2\",\"create_date\":\"2015-11-22 11:05:43\"},{\"id\":\"3\",\"iduser\":\"1\",\"idinterest\":\"3\",\"create_date\":\"2015-11-22 11:05:43\"},{\"id\":\"6\",\"iduser\":\"1\",\"idinterest\":\"2\",\"create_date\":\"2015-11-28 19:56:59\"},{\"id\":\"7\",\"iduser\":\"1\",\"idinterest\":\"3\",\"create_date\":\"2015-11-28 19:56:59\"},{\"id\":\"8\",\"iduser\":\"1\",\"idinterest\":\"4\",\"create_date\":\"2015-11-28 19:56:59\"}],\"user_pics\":[{\"title\":\"test upload\",\"description\":\"this is a description\",\"pic\":\"http:\\/\\/xperpendicular.com\\/lokaso\\/user_uploads\\/56536b02d08d2.jpg\",\"create_date\":\"2015-11-23 19:37:38\"},{\"title\":\"my first upload\",\"description\":\"this is just a test upload\",\"pic\":\"http:\\/\\/xperpendicular.com\\/lokaso\\/user_uploads\\/565a0b4011ab5.jpg\",\"create_date\":\"2015-11-28 20:14:56\"}],\"user_asks\":[{\"id\":\"1\",\"iduser\":\"1\",\"idinterest\":\"1\",\"ask_title\":\"Water sport avenues in Mumbai\",\"ask_desc\":\"What are the best options for water sports in Mumbai\",\"lat\":\"18.97500000\",\"lng\":\"72.82580000\",\"status\":\"1\",\"cnt_response\":\"1\",\"create_date\":\"2015-11-22 11:05:43\",\"modified_date\":\"0000-00-00 00:00:00\",\"name\":\"Ram Singh\",\"email\":\"xx@xx.com\",\"pic\":\"http:\\/\\/xperpendicular.com\\/lokaso\\/user_pics\\/rohit.jpg\",\"distance\":\"0\"}],\"user_following\":[{\"user_id\":\"1\",\"name\":\"Rohit Sharma\",\"email\":\"ww@ww.com\",\"distance\":\"0\",\"create_date\":\"2015-11-28 19:56:59\",\"user_pic\":\"http:\\/\\/xperpendicular.com\\/lokaso\\/user_pics\\/rohit.jpg\",\"lat\":\"15.49890000\",\"lng\":\"73.82780000\",\"num_asks\":\"1\",\"num_responses\":\"0\"}]}");
            JSONArray usersArray = response.getJSONArray("user_data");
            Log.v("usersArray",usersArray.toString());
            arrayLength = usersArray.length();
            if (arrayLength > 0) {
                try {
                JSONArray userPics = response.getJSONArray("user_pics");

                for (int i = 0; i < userPics.length(); i++) {
                    UserPhotos userPhotos = new UserPhotos(userPics.getJSONObject(i));
                    streamListUserPhotos.add(userPhotos);
                }
                } catch (JSONException e) {

                    e.printStackTrace();

                }
                    try {
                JSONArray userAsks = response.getJSONArray("user_asks");

                for (int i = 0; i < userAsks.length(); i++) {
                    UserAsks userAsksModel = new UserAsks(userAsks.getJSONObject(i));
                    streamListAsks.add(userAsksModel);
                }
                    } catch (JSONException e) {

                        e.printStackTrace();

                    }
                        try {
                JSONArray userFollowing = response.getJSONArray("user_following");
                for (int i = 0; i < userFollowing.length(); i++) {
                    UserFriends userFollowingModel = new UserFriends(userFollowing.getJSONObject(i));
                    streamListFollowing.add(userFollowingModel);
                }
                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
            }
        } catch (JSONException e) {

            e.printStackTrace();

        } finally {
            Log.v("notify datachange","notify datachange");
            adapter.notifyDataSetChanged();
        }


    }*/
    public void getData() {
        showLoadingScreen();
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_PROFILE_GET+profile_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            streamListUserPhotos.clear();
                            streamListAsks.clear();
                            streamListFollowing.clear();
                            Log.v("response",response.toString());
                                JSONObject usersArray = response.getJSONObject("user_data");
                                profileName = usersArray.getString("name");

                                if(!String.valueOf(App.getInstance().getId()).equalsIgnoreCase(profile_id)){
                                    toolbar.setTitle(profileName);
                                    statusButton.setText("Edit");
                                }
                            //String photos, String asks, String followers, String userPic
                            setProfileData(response.getString("total_pics"), response.getString("total_asks"), response.getString("total_followers"), usersArray.getString("user_pic"));
                                Log.v("usersArray",usersArray.toString());
                                 JSONArray userPics = response.getJSONArray("user_pics");

                                    for (int i = 0; i < userPics.length(); i++) {
                                        UserPhotos userPhotos = new UserPhotos(userPics.getJSONObject(i));
                                        streamListUserPhotos.add(userPhotos);
                                    }

                                    JSONArray userAsks = response.getJSONArray("user_asks");

                                    for (int i = 0; i < userAsks.length(); i++) {
                                        UserAsks userAsksModel = new UserAsks(userAsks.getJSONObject(i));
                                        streamListAsks.add(userAsksModel);
                                        Log.v("userAsksModel",String.valueOf(streamListAsks.size()));
                                    }
                                    JSONArray userFollowing = response.getJSONArray("user_following");
                                    for (int i = 0; i < userFollowing.length(); i++) {
                                        UserFriends userFollowingModel = new UserFriends(userFollowing.getJSONObject(i));
                                        streamListFollowing.add(userFollowingModel);
                                    }


                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            removeLoadingScreen();
                            Log.v("notify datachange","notify datachange");
                            adapter = new PagerAdapterProfile(getSupportFragmentManager(), tabLayout.getTabCount(), streamListUserPhotos, streamListFollowing, streamListAsks);
                            viewPager.setAdapter(adapter);
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
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("profileId", profile_id);

                return params;

            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    private void setProfileData(String photos, String asks, String followers, String userPic){
        userPhotos.setText(photos);
        userAsks.setText(asks);
        userFollowers.setText(followers);
        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }
        if (imageLoader != null) {
            imageLoader.get(userPic, ImageLoader.getImageListener(imageView, R.drawable.profile_default_photo, R.drawable.profile_default_photo));
        }
    }

    public void showLoadingScreen() {

        getSupportActionBar().setTitle(getText(R.string.title_activity_profile));

        mProfileLoadingScreen.setVisibility(View.VISIBLE);

    }

    public void removeLoadingScreen() {

        mProfileLoadingScreen.setVisibility(View.GONE);

    }

    public void uploadUserPic(View v) {

        Intent intent = new Intent(ProfileActivityNew.this, UserPicUpload.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
