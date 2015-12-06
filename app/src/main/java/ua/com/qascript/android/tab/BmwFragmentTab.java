package ua.com.qascript.android.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ua.com.qascript.android.R;
import ua.com.qascript.android.adapter.UserAsksListAdapter;
import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.model.UserAsks;
import ua.com.qascript.android.model.UserFriends;
import ua.com.qascript.android.util.CustomRequest;

public class BmwFragmentTab extends Fragment  implements Constants {
    int offset = 0;
    int limit = 10;
    int pageNo = 0;
    int arrayLength = 0;
    private ArrayList<UserAsks> streamList;
    UserAsksListAdapter userFriendsListAdapter;
    private ListView saveListView;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bmw_layout, container, false);
        streamList = new ArrayList<UserAsks>();
        if (App.getInstance().isConnected()) {
            getStream();
        }
        searchView = (SearchView)rootView.findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(queryTextListener);
        saveListView = (ListView)rootView.findViewById(R.id.saveListView);
        userFriendsListAdapter = new UserAsksListAdapter(getActivity(), streamList, null);
        saveListView.setAdapter(userFriendsListAdapter);

        return rootView;
    }

    public void getStream() {
        long profileId = App.getInstance().getId();
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_ASKS_GET+1, null,
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

                                        UserAsks answer = new UserAsks(answerObj);

                                        streamList.add(answer);
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            userFriendsListAdapter.notifyDataSetChanged();
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
    SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (query.length() > 0) {
                Toast.makeText(getActivity().getApplicationContext(), "Clicked[" + query + "]", Toast.LENGTH_LONG).show();
            }

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {


            return false;
        }

    };
}
