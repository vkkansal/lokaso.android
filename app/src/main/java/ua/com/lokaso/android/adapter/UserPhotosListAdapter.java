package ua.com.lokaso.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.UserPhotos;
import ua.com.lokaso.android.util.AnswerInterface;

public class UserPhotosListAdapter extends BaseAdapter implements Constants{

	private Activity activity;
	private LayoutInflater inflater;
	private List<UserPhotos> answersList;

   private AnswerInterface responder;

    ImageLoader imageLoader = App.getInstance().getImageLoader();

	public UserPhotosListAdapter(Activity activity, List<UserPhotos> answersList, AnswerInterface responder) {

		this.activity = activity;
		this.answersList = answersList;
        this.responder = responder;

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
        public ImageView imageUser;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.user_photos_list_row, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.username = (TextView) convertView.findViewById(R.id.username);
            viewHolder.imageUser = (ImageView) convertView.findViewById(R.id.imageUser);

            convertView.setTag(viewHolder);

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

        if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }

        final UserPhotos answer = answersList.get(position);

        if (answer.getPic().length() != 0) {

            viewHolder.imageUser.setImageResource(R.drawable.profile_default_photo);
            imageLoader.get(answer.getPic(), ImageLoader.getImageListener(viewHolder.imageUser, R.drawable.profile_default_photo, R.drawable.profile_default_photo));

        } else {

            viewHolder.imageUser.setImageResource(R.drawable.profile_default_photo);
        }

        viewHolder.username.setText(answer.getTitle());
		Log.v("getView",answer.getTitle());
		return convertView;
	}
}