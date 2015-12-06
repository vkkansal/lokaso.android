package ua.com.qascript.android.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;
import java.util.Map;

import ua.com.qascript.android.R;
import ua.com.qascript.android.adapter.UserFriendsListAdapter;
import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.model.UserFriends;
import ua.com.qascript.android.util.CustomRequest;

public class UserFollowingTabProfile extends Fragment implements Constants {

    int offset = 0;
    int limit = 10;
    int pageNo = 0;
    int arrayLength = 0;
    private List<UserFriends> streamList;
    UserFriendsListAdapter userFriendsListAdapter;
    private ListView saveListView;
    SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_photos, container, false);
        saveListView = (ListView)rootView.findViewById(R.id.saveListView);
        userFriendsListAdapter = new UserFriendsListAdapter(getActivity(), streamList, null);
        saveListView.setAdapter(userFriendsListAdapter);
        return rootView;
    }

    public static UserFollowingTabProfile newInstance(List<UserFriends> streamList) {
        UserFollowingTabProfile myFragment = new UserFollowingTabProfile();
        myFragment.streamList = streamList;
        return myFragment;
    }
}
