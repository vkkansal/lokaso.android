package ua.com.lokaso.android.tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.adapter.UserFriendsListAdapter;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserFriends;
import ua.com.lokaso.android.util.CustomRequest;
import ua.com.lokaso.android.view.ProfileActivityNew;

public class FolksFragmentTab extends Fragment implements Constants {

    int offset = 0;
    int limit = 10;
    //int pageNo = 0;
    int arrayLength = 0;
    private List<UserFriends> streamList;
    UserFriendsListAdapter userFriendsListAdapter;
    private ListView saveListView;
    SearchView searchView;
    Boolean loadingMore = false;
    Boolean viewMore = false;

    public static FolksFragmentTab newInstance(List<UserFriends> streamList) {
        FolksFragmentTab myFragment = new FolksFragmentTab();
        myFragment.streamList = streamList;
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.toyota_layout, container, false);
        searchView = (SearchView)rootView.findViewById(R.id.searchView1);
        searchView.setOnQueryTextListener(queryTextListener);
        saveListView = (ListView)rootView.findViewById(R.id.saveListView);
        userFriendsListAdapter = new UserFriendsListAdapter(getActivity(), streamList, null, onClickListener, onFollowerClickListener);

        saveListView.setAdapter(userFriendsListAdapter);
        loadingComplete(streamList.size());
        saveListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastInScreen = firstVisibleItem + visibleItemCount;
                Log.v("lastInScreen",String.valueOf(lastInScreen));
                Log.v("totalItemCount",String.valueOf(totalItemCount));
                Log.v("loadingMore",String.valueOf(loadingMore));
                Log.v("viewMore",String.valueOf(viewMore));
                if ((lastInScreen == totalItemCount) && !(loadingMore) && (viewMore)) {

                    if (App.getInstance().isConnected()) {

                        loadingMore = true;

                        getStream();
                    }
                }
            }
        });

        return rootView;
    }
    View.OnClickListener onFollowerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ImageView followerImage = (ImageView)v;
            followerImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_following));


        }
    };
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UserFriendsListAdapter.ViewHolder vh = (UserFriendsListAdapter.ViewHolder)v.getTag();
            Intent i = new Intent(getActivity(), ProfileActivityNew.class);
            i.putExtra("profileId",vh.id);
            startActivity(i);
        }
    };

    public void getStream() {
        long profileId = App.getInstance().getId();
        CustomRequest jsonReq = new CustomRequest(Request.Method.GET, METHOD_USERS_FOLKS_GET+profileId, null,
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
                                        UserFriends answer = new UserFriends(answerObj);

                                        streamList.add(answer);
                                    }
                                }

                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {
                            loadingMore = false;
                            loadingComplete(arrayLength);
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
                offset = offset +limit;
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void loadingComplete(int arrayLength) {

        if (arrayLength == limit) {

            viewMore = true;

        } else {

            viewMore = false;
        }
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
