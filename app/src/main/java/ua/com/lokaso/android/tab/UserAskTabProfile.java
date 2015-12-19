package ua.com.lokaso.android.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.adapter.UserAsksListAdapter;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserAsks;

public class UserAskTabProfile extends Fragment  implements Constants {
    int offset = 0;
    int limit = 10;
    int pageNo = 0;
    int arrayLength = 0;
    private List<UserAsks> streamList;
    UserAsksListAdapter userFriendsListAdapter;
    private ListView saveListView;
    SearchView searchView;
    static UserAskTabProfile myFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_photos, container, false);
        saveListView = (ListView)rootView.findViewById(R.id.saveListView);
        Log.v("streamList",String.valueOf(streamList.size()));
        userFriendsListAdapter = new UserAsksListAdapter(getActivity(), streamList, null, onClickListener);
        saveListView.setAdapter(userFriendsListAdapter);

        return rootView;
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "clicked userasktab", Toast.LENGTH_SHORT).show();
        }
    };
    public static UserAskTabProfile newInstance(List<UserAsks> streamList) {
        if(myFragment==null) {
            myFragment = new UserAskTabProfile();
        }
        myFragment.streamList = streamList;
        return myFragment;
    }
}
