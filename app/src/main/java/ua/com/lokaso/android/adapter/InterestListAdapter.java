package ua.com.lokaso.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import ua.com.lokaso.android.R;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.Interest;
import ua.com.lokaso.android.util.AnswerInterface;
import ua.com.lokaso.android.util.TagClick;

public class InterestListAdapter extends BaseAdapter implements Constants, TagClick {

	private Activity activity;
	private LayoutInflater inflater;
	private List<Interest> answersList;

    private AnswerInterface responder;
	DisplayImageOptions defaultOptions;


	//com.android.volley.toolbox.ImageLoader imageLoader = App.getInstance().getImageLoader();

	public InterestListAdapter(Activity activity, List<Interest> answersList, AnswerInterface responder) {

		this.activity = activity;
		this.answersList = answersList;
        this.responder = responder;


		defaultOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(false).imageScaleType(ImageScaleType.EXACTLY)
                .resetViewBeforeLoading(true)
				/*.displayer(new RoundedBitmapDisplayer(8))*/
				.build();// SimpleBitmapDisplayer()).build();


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

        public TextView listview_TeamDescription;
        public ImageView iconinterest;
		public RelativeLayout selector;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;

		if (inflater == null) {

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.interest_item, null);
			
			viewHolder = new ViewHolder();
			
			viewHolder.iconinterest = (ImageView) convertView.findViewById(R.id.iconinterest);
			viewHolder.selector = (RelativeLayout) convertView.findViewById(R.id.myCheckBox1);
            viewHolder.listview_TeamDescription = (TextView) convertView.findViewById(R.id.listview_TeamDescription);
            convertView.setTag(viewHolder);

		} else {
			
			viewHolder = (ViewHolder) convertView.getTag();
		}

       /* if (imageLoader == null) {

            imageLoader = App.getInstance().getImageLoader();
        }*/

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewGroup vg = (ViewGroup) v;


				RelativeLayout img = (RelativeLayout)((ViewGroup) v).getChildAt(1);
				if(img.getVisibility()== View.VISIBLE){
					img.setVisibility(View.GONE);
				}else{
					img.setVisibility(View.VISIBLE);
				}
		}
		});
//        viewHolder.questionRemove.setTag(position);
        viewHolder.listview_TeamDescription.setTag(position);
        viewHolder.iconinterest.setTag(position);

        final Interest answer = answersList.get(position);

        if (answer.getInterest_pic().length() != 0) {

			viewHolder.iconinterest.setImageResource(R.drawable.profile_default_photo);
			ImageLoader.getInstance().displayImage(answer.getInterest_pic(), viewHolder.iconinterest, defaultOptions);
            //imageLoader.get(answer.getInterest_pic(), com.android.volley.toolbox.ImageLoader.getImageListener(viewHolder.iconinterest, R.drawable.profile_default_photo, R.drawable.profile_default_photo));
			//ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
			//ImageLoader.getInstance().displayImage(answer.getInterest_pic(), viewHolder.iconinterest, defaultOptions);

        }/* else {

            //viewHolder.iconinterest.setImageResource(R.drawable.profile_default_photo);
			imageLoader.displayImage(answer.getInterest_pic(), viewHolder.iconinterest, defaultOptions);
        }*/

        viewHolder.listview_TeamDescription.setText(answer.getName());

        return convertView;
	}


    @Override
    public void clickedTag(CharSequence tag) {
        // TODO Auto-generated method stub

       /* Intent i = new Intent(activity, HashtagsActivity.class);
        i.putExtra("hashtag", tag);
        activity.startActivity(i);*/
    }
}