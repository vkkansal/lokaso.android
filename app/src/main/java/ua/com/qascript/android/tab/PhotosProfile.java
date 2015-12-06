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
import ua.com.qascript.android.adapter.UserAsksListAdapter;
import ua.com.qascript.android.adapter.UserPhotosListAdapter;
import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.model.UserAsks;
import ua.com.qascript.android.model.UserPhotos;
import ua.com.qascript.android.util.CustomRequest;

public class PhotosProfile extends Fragment  implements Constants {
    private List<UserPhotos> streamList;
    UserPhotosListAdapter userFriendsListAdapter;
    private ListView saveListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_photos, container, false);
        saveListView = (ListView)rootView.findViewById(R.id.saveListView);
        userFriendsListAdapter = new UserPhotosListAdapter(getActivity(), streamList, null);
        saveListView.setAdapter(userFriendsListAdapter);
        return rootView;
    }

    public static PhotosProfile newInstance(List<UserPhotos> streamList) {
        PhotosProfile myFragment = new PhotosProfile();
        myFragment.streamList = streamList;
        return myFragment;
    }
}
