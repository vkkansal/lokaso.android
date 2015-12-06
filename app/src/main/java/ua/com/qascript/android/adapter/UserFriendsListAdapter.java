package ua.com.qascript.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.com.qascript.android.HashtagsActivity;
import ua.com.qascript.android.PhotoViewActivity;
import ua.com.qascript.android.ProfileActivity;
import ua.com.qascript.android.R;
import ua.com.qascript.android.app.App;
import ua.com.qascript.android.constants.Constants;
import ua.com.qascript.android.model.Answer;
import ua.com.qascript.android.model.UserFriends;
import ua.com.qascript.android.util.AnswerInterface;
import ua.com.qascript.android.util.CustomRequest;
import ua.com.qascript.android.util.TagClick;
import ua.com.qascript.android.util.TagSelectingTextview;
import ua.com.qascript.android.view.ResizableImageView;

public class UserFriendsListAdapter extends BaseAdapter implements Constants, TagClick {

    private Activity activity;
    private LayoutInflater inflater;
    private List<UserFriends> answersList;

    //TagSelectingTextview mTagSelectingTextview;

    private AnswerInterface responder;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

    public UserFriendsListAdapter(Activity activity, List<UserFriends> answersList, AnswerInterface responder) {

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

    static class ViewHolder {

        public TextView folksName;
        public TextView folksAsks;
        public TextView folksResponses;
        public TextView folksDistance;
        public ImageView folksImage;
        public RatingBar folksRating;
        public ImageView folkFollowingType;
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

            /*viewHolder.answerAuthor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int getPosition = (Integer) v.getTag();
                    ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.answerAction);
                    // TODO Auto-generated method stub

                    UserFriends answer = answersList.get(getPosition);

                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("profileId", answer.getId());
                    activity.startActivity(intent);
                }
            });*/

            /*viewHolder.questionAuthor.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int getPosition = (Integer) v.getTag();

                    UserFriends answer = answersList.get(getPosition);

                    Intent intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra("profileId", answer.getFromUserId());
                    activity.startActivity(intent);
                }
            });*/

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

//        viewHolder.questionRemove.setTag(position);
        /*viewHolder.questionText.setTag(position);
        viewHolder.answerAuthor.setTag(position);
        viewHolder.answerText.setTag(position);
        viewHolder.answerAuthor.setTag(R.id.answerAuthor, viewHolder);*/

        final UserFriends answer = answersList.get(position);

        if (answer.getUser_pic().length() != 0) {

            viewHolder.folksImage.setImageResource(R.drawable.profile_default_photo);
            imageLoader.get(answer.getUser_pic(), ImageLoader.getImageListener(viewHolder.folksImage, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.folksImage.setImageResource(R.drawable.profile_default_photo);
        }

        viewHolder.folksName.setText(answer.getName());

        viewHolder.folksAsks.setText(answer.getNum_asks()+" Asks");
        viewHolder.folksResponses.setText(answer.getNum_responses()+" Responses");
        viewHolder.folksDistance.setText(answer.getDistance());

        return convertView;
    }

    @Override
    public void clickedTag(CharSequence tag) {
        // TODO Auto-generated method stub

        Intent i = new Intent(activity, HashtagsActivity.class);
        i.putExtra("hashtag", tag);
        activity.startActivity(i);
    }
}