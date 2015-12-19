package ua.com.lokaso.android.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ua.com.lokaso.android.model.UserAsks;
import ua.com.lokaso.android.model.UserFriends;

public class PagerAdapterDashboard extends FragmentStatePagerAdapter{
    int mNumOfTabs;
    private List<UserFriends> streamListFollowing;
    private List<UserAsks> streamListAsks;

    public PagerAdapterDashboard(FragmentManager fm, int NumOfTabs, List<UserFriends> streamListFollowing, List<UserAsks> streamListAsks) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.streamListFollowing = streamListFollowing;
        this.streamListAsks = streamListAsks;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FolksFragmentTab tab1 = FolksFragmentTab.newInstance(streamListFollowing);
                return tab1;
            case 1:
                AsksFragmentTab tab2 = AsksFragmentTab.newInstance(streamListAsks);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
