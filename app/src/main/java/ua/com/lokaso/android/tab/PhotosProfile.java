package ua.com.lokaso.android.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.adapter.UserPhotosListAdapter;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserPhotos;

public class PhotosProfile extends Fragment  implements Constants {
    private List<UserPhotos> streamList;
    UserPhotosListAdapter userFriendsListAdapter;
    private ListView saveListView;
    static PhotosProfile myFragment;
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
        if(myFragment==null) {
            myFragment = new PhotosProfile();
        }
        myFragment.streamList = streamList;
        return myFragment;
    }
}
