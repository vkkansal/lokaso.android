package ua.com.lokaso.android.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import ua.com.lokaso.android.model.UserAsks;
import ua.com.lokaso.android.model.UserFriends;
import ua.com.lokaso.android.model.UserPhotos;

public class PagerAdapterProfile extends FragmentPagerAdapter {
    int mNumOfTabs;
    private List<UserPhotos> streamListUserPhotos;
    private List<UserFriends> streamListFollowing;
    private List<UserAsks> streamListAsks;
    public PagerAdapterProfile(FragmentManager fm, int NumOfTabs,List<UserPhotos> streamListUserPhotos, List<UserFriends> streamListFollowing, List<UserAsks> streamListAsks) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.streamListUserPhotos = streamListUserPhotos;
        this.streamListFollowing = streamListFollowing;
        this.streamListAsks = streamListAsks;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PhotosProfile tab1 = PhotosProfile.newInstance(streamListUserPhotos);
                return tab1;
            case 1:
                UserAskTabProfile tab2 = UserAskTabProfile.newInstance(streamListAsks);
                return tab2;
            case 2:
                UserFollowingTabProfile tab3 = UserFollowingTabProfile.newInstance(streamListFollowing);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
