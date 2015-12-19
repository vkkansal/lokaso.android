package ua.com.lokaso.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserAsks;
import ua.com.lokaso.android.util.AnswerInterface;
import ua.com.lokaso.android.util.TagClick;
import ua.com.lokaso.android.util.TagSelectingTextview;

public class UserAsksListAdapter extends BaseAdapter implements Constants, TagClick {

	private Activity activity;
	private LayoutInflater inflater;
	private List<UserAsks> answersList;
    View.OnClickListener onClickListener;

    TagSelectingTextview mTagSelectingTextview;

    private AnswerInterface responder;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

	public UserAsksListAdapter(Activity activity, List<UserAsks> answersList, AnswerInterface responder,View.OnClickListener onClickListener) {

        this.onClickListener = onClickListener;
		this.activity = activity;
		this.answersList = answersList;
        this.responder = responder;

        mTagSelectingTextview = new TagSelectingTextview();
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

        public TextView username;
        public TextView titleText;
        public Button replyTo;
        public TextView askNoResponses;
        public TextView askDistance;
        public ImageView imageUser;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.user_asks_list_row, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.titleText = (TextView) convertView.findViewById(R.id.titleText);
            viewHolder.askNoResponses = (TextView) convertView.findViewById(R.id.no_responses);
            viewHolder.askDistance = (TextView) convertView.findViewById(R.id.ask_distance);
            viewHolder.replyTo = (Button) convertView.findViewById(R.id.replyTo);
            viewHolder.imageUser = (ImageView) convertView.findViewById(R.id.imageUser);

            convertView.setTag(viewHolder);

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        final UserAsks answer = answersList.get(position);

        if (answer.getUser_pic().length() != 0) {

            viewHolder.imageUser.setImageResource(R.drawable.profile_default_photo);
            //String baseUrl = "http://52.91.86.27/user_pics/";
            imageLoader.get(answer.getUser_pic(), ImageLoader.getImageListener(viewHolder.imageUser, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.imageUser.setImageResource(R.drawable.profile_default_photo);
        }

        viewHolder.username.setText(answer.getName());
        viewHolder.titleText.setText(answer.getAsk_title() +" Asks");
        viewHolder.askDistance.setText(answer.getDistance()+" KM");
        viewHolder.askNoResponses.setText(answer.getCnt_response() +" Responses");
        convertView.setOnClickListener(onClickListener);
        return convertView;
	}

    @Override
    public void clickedTag(CharSequence tag) {

    }
}