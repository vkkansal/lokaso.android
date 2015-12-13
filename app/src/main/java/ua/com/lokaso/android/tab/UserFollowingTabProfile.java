package ua.com.lokaso.android.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.adapter.UserFriendsListAdapter;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserFriends;

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
        userFriendsListAdapter = new UserFriendsListAdapter(getActivity(), streamList, null, onClickListener,null);
        saveListView.setAdapter(userFriendsListAdapter);
        return rootView;
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "clicked TOYOTA", Toast.LENGTH_SHORT).show();

        }
    };
    public static UserFollowingTabProfile newInstance(List<UserFriends> streamList) {
        UserFollowingTabProfile myFragment = new UserFollowingTabProfile();
        myFragment.streamList = streamList;
        return myFragment;
    }
}
