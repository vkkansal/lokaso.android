package ua.com.lokaso.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserFriends;
import ua.com.lokaso.android.util.AnswerInterface;
import ua.com.lokaso.android.util.TagClick;

public class UserFriendsListAdapter extends BaseAdapter implements Constants, TagClick {

    private Activity activity;
    private LayoutInflater inflater;
    private List<UserFriends> answersList;

    //TagSelectingTextview mTagSelectingTextview;

    private AnswerInterface responder;
    View.OnClickListener onClickListener;
    View.OnClickListener onFollowerClickListener;
    ImageLoader imageLoader = App.getInstance().getImageLoader();

    public UserFriendsListAdapter(Activity activity, List<UserFriends> answersList, AnswerInterface responder,View.OnClickListener onClickListener,View.OnClickListener onFollowerClickListener) {
        this.onClickListener = onClickListener;
        this.onFollowerClickListener = onFollowerClickListener;
        this.activity = activity;
        this.answersList = answersList;
        this.responder = responder;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //mTagSelectingTextview = new TagSelectingTextview();
    }

    @Override
    public int getCount() {

        return answersList.size();
    }

    @Override
    public Object getItem(int location) {

        return answersList.get(location);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class ViewHolder {

        public TextView folksName;
        public TextView folksAsks;
        public TextView folksResponses;
        public TextView folksDistance;
        public ImageView folksImage;
        public RatingBar folksRating;
        public ImageView folkFollowingType;
        public String id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;


        if (convertView == null) {

            convertView = inflater.inflate(R.layout.folks_single_item, null);//user_friends_list_row, null);

            viewHolder = new ViewHolder();

            viewHolder.folksImage = (ImageView) convertView.findViewById(R.id.folks_urer_img);//answerAuthor);
            viewHolder.folksName = (TextView) convertView.findViewById(R.id.folks_user_name);
            viewHolder.folksDistance = (TextView) convertView.findViewById(R.id.folks_user_distance);
            viewHolder.folksAsks = (TextView) convertView.findViewById(R.id.folks_user_ask);
            viewHolder.folksResponses = (TextView) convertView.findViewById(R.id.folks_user_responses);
            viewHolder.folksRating = (RatingBar) convertView.findViewById(R.id.folks_user_ratings_image);
            viewHolder.folkFollowingType = (ImageView) convertView.findViewById(R.id.folks_user_following_type);

            convertView.setTag(viewHolder);



        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }


        final UserFriends answer = answersList.get(position);

        if (answer.getUser_pic().length() != 0) {

            viewHolder.folksImage.setImageResource(R.drawable.profile_default_photo);
            imageLoader.get(answer.getUser_pic(), ImageLoader.getImageListener(viewHolder.folksImage, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.folksImage.setImageResource(R.drawable.profile_default_photo);
        }
        if(answer.getIs_following().equalsIgnoreCase("0"))
        {
            viewHolder.folkFollowingType.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_follow));

        }else{
            viewHolder.folkFollowingType.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_following));
        }
        if(onFollowerClickListener!=null) {
            viewHolder.folkFollowingType.setOnClickListener(onFollowerClickListener);
        }
        viewHolder.id = answer.getId();
        convertView.setOnClickListener(onClickListener);
        viewHolder.folksName.setText(answer.getName());

        viewHolder.folksAsks.setText(answer.getNum_asks()+" Asks");
        viewHolder.folksResponses.setText(answer.getNum_responses()+" Responses");
        viewHolder.folksDistance.setText(answer.getDistance());

        return convertView;
    }

    @Override
    public void clickedTag(CharSequence tag) {

    }
}