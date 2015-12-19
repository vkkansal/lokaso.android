package ua.com.lokaso.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.com.lokaso.android.adapter.InterestListAdapter;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.Interest;
import ua.com.lokaso.android.tab.DashboardActivityTab;
import ua.com.lokaso.android.util.CustomRequest;

public class InterestListView extends AppCompatActivity implements Constants{
    ArrayList<Interest> streamList;
    InterestListAdapter myAdapter;
    GridView myListView;
    int offset = 0;
    int limit = 10;
    int pageNo = 0;
    int arrayLength = 0;
    Button submitbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_list_layout);
        /*// UNIVERSAL IMAGE LOADER SETUP
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY).resetViewBeforeLoading(true)
                .displayer(new RoundedBitmapDisplayer(8))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        streamList = new ArrayList<Interest>();
        myAdapter = new InterestListAdapter(this, streamList, null );
        myListView = (GridView)findViewById(R.id.listinterest);
        myListView.setAdapter(myAdapter);
        getStream();
        submitbtn = (Button) findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                gotoMainActivity();
            }
        });

    }


    private void gotoMainActivity(){
        ActivityCompat.finishAffinity(InterestListView.this);
        Intent i = new Intent(InterestListView.this, DashboardActivityTab.class);
        i.putExtra("profileId", App.getInstance().getId());
        startActivity(i);
    }

    public void getStream() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_INTERESTS_GET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            arrayLength = 0;
                            if (response != null) {

                                JSONArray usersArray = response.getJSONArray("users");

                                arrayLength = usersArray.length();
                                if (arrayLength > 0) {

                                    for (int i = 0; i < usersArray.length(); i++) {

                                        JSONObject answerObj = (JSONObject) usersArray.get(i);

                                        Interest answer = new Interest(answerObj);

                                        streamList.add(answer);
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            myAdapter.notifyDataSetChanged();
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

    public void setUserInterest() {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_USERS_INTERESTS_SET, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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
