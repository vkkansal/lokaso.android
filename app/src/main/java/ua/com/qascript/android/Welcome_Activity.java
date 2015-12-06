package ua.com.qascript.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import ua.com.qascript.android.app.App;
import ua.com.qascript.android.common.ActivityBase;
import ua.com.qascript.android.service.RegistrationIntentService;
import ua.com.qascript.android.tab.MainActivityTab;
import ua.com.qascript.android.util.CustomRequest;

public class Welcome_Activity  extends ActivityBase
{

	TextView s;

	TextView loginBtn, signupBtn;

	TextView appTextLabel;

	LoginButton loginButton;

	private ViewPager viewPager;

	CallbackManager callbackManager;

	private MyViewPagerAdapter myViewPagerAdapter;

	private ArrayList<Integer> listOfItems;

	ImageLoader imageLoader = App.getInstance().getImageLoader();

	String facebookId = "", facebookName = "", facebookEmail = "";

	String key;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());

		startService(new Intent(this, RegistrationIntentService.class));
		setContentView(R.layout.activity_walkthrough);
		loginBtn = (TextView) findViewById(R.id.loginBtn);
		signupBtn = (TextView) findViewById(R.id.signupBtn);
		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("user_friends, email");
		appTextLabel = (TextView) findViewById(R.id.appTextLabel);

		hideButtons();

		if (AccessToken.getCurrentAccessToken()!= null) LoginManager.getInstance().logOut();

		callbackManager = CallbackManager.Factory.create();
		Intent i = getIntent();
		facebookId = i.getStringExtra("facebookId");
		facebookName = i.getStringExtra("facebookName");
		facebookEmail = i.getStringExtra("facebookEmail");

		loginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ActivityCompat.finishAffinity(Welcome_Activity.this);
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
			}
		});

		signupBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//ActivityCompat.finishAffinity(Welcome_Activity.this);
				Intent i = new Intent(getApplicationContext(), SignupActivity.class);
				startActivity(i);
			}
		});
		listOfItems = new ArrayList<Integer>();
		listOfItems.add(R.drawable.travel);
		listOfItems.add(R.drawable.travel);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		setViewPagerItemsWithAdapter();

		LoginManager.getInstance().registerCallback(callbackManager,
				new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {

						// App code

						if (App.getInstance().isConnected()) {

							showpDialog();

							GraphRequest request = GraphRequest.newMeRequest(
									AccessToken.getCurrentAccessToken(),
									new GraphRequest.GraphJSONObjectCallback() {
										@Override
										public void onCompleted(JSONObject object, GraphResponse response) {

											// Application code

											try {

												facebookId = object.getString("id");
												facebookName = object.getString("name");

												if (object.has("email")) {

													facebookEmail = object.getString("email");
												}

											} catch (Throwable t) {

												Log.e("Profile", "Could not parse malformed JSON: \"" + object.toString() + "\"");

											} finally {

												if (AccessToken.getCurrentAccessToken() != null)
													LoginManager.getInstance().logOut();

												Log.d("Profile", object.toString());

												if (App.getInstance().isConnected()) {

													if (!facebookId.equals("")) {

														signinByFacebookId();

													} else {

														goToSignUpPage();
													}

												} else {

													goToSignUpPage();
												}
											}
										}
									});
							Bundle parameters = new Bundle();
							parameters.putString("fields", "id,name,link,email");
							request.setParameters(parameters);
							request.executeAsync();
						}
					}

					@Override
					public void onCancel() {

						// App code
						// Cancel
					}

					@Override
					public void onError(FacebookException exception) {

						// App code
						// Error
					}
				});
	}

	private void setViewPagerItemsWithAdapter()
	{

		myViewPagerAdapter = new MyViewPagerAdapter(listOfItems);
		viewPager.setAdapter(myViewPagerAdapter);
		viewPager.setCurrentItem(0);
	}

	// adapter
	public class MyViewPagerAdapter extends PagerAdapter
	{

		private LayoutInflater layoutInflater;

		private ArrayList<Integer> items;

		public MyViewPagerAdapter(ArrayList<Integer> listOfItems)
		{

			this.items = listOfItems;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{

			layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.view_pager_item, container, false);
			ImageView tView = (ImageView) view.findViewById(R.id.PageNumber);
			tView.setImageResource(listOfItems.get(position));
			((ViewPager) container).addView(view);
			return view;
		}

		@Override
		public int getCount()
		{

			return items.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj)
		{

			return view == ((View) obj);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{

			View view = (View) object;
			((ViewPager) container).removeView(view);
		}
	}

	@Override
	protected void  onStart() {

		super.onStart();

		if (!App.getInstance().isConnected()) {

			showLabel();

		} else if (App.getInstance().isConnected() && App.getInstance().getId() != 0) {

			CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_AUTH_AUTHORIZE_POST, null,
					new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {

							if (App.getInstance().authorize(response)) {

								if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

									ActivityCompat.finishAffinity(Welcome_Activity.this);

									Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
									i.putExtra("profileId", App.getInstance().getId());
									startActivity(i);

								} else {

									App.getInstance().logout();
									showButtons();
								}

							} else {

								showButtons();
							}
						}
					}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

					showButtons();
				}
			}) {

				@Override
				protected Map<String, String> getParams() {
					Map<String, String> params = new HashMap<String, String>();
					params.put("accountId", Long.toString(App.getInstance().getId()));
					params.put("accessToken", App.getInstance().getAccessToken());
					params.put("gcm_regId", App.getInstance().getGcmToken());

					return params;
				}
			};

			App.getInstance().addToRequestQueue(jsonReq);

		} else {

			showButtons();
		}
	}

	public void showLabel() {

		appTextLabel.setVisibility(View.VISIBLE);
	}

	public void showButtons() {
		loginButton.setVisibility(View.VISIBLE);
		loginBtn.setVisibility(View.VISIBLE);
		signupBtn.setVisibility(View.VISIBLE);
	}

	public void hideButtons() {
		loginButton.setVisibility(View.GONE);
		loginBtn.setVisibility(View.GONE);
		signupBtn.setVisibility(View.GONE);
	}

	public void signinByFacebookId() {

		CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_AUTH_FACEBOOK_POST,  null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						if (App.getInstance().authorize(response)) {

							if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED) {

								ActivityCompat.finishAffinity(Welcome_Activity.this);

								Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
								startActivity(i);

							} else {

								ActivityCompat.finishAffinity(Welcome_Activity.this);

								Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
								i.putExtra("facebookId",facebookId);
								i.putExtra("facebookName", facebookName);
								i.putExtra("facebookEmail", facebookEmail);
								startActivity(i);

							}

						} else {

							ActivityCompat.finishAffinity(Welcome_Activity.this);

							Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
							i.putExtra("facebookId",facebookId);
							i.putExtra("facebookName", facebookName);
							i.putExtra("facebookEmail", facebookEmail);
							startActivity(i);
						}

						hidepDialog();
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

				Toast.makeText(getApplicationContext(), getText(R.string.error_data_loading), Toast.LENGTH_LONG).show();

				hidepDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("facebookId", facebookId);
				params.put("clientId", CLIENT_ID);
				params.put("gcm_regId", App.getInstance().getGcmToken());

				return params;
			}
		};

		App.getInstance().addToRequestQueue(jsonReq);
	}

	private void goToSignUpPage(){
		ActivityCompat.finishAffinity(Welcome_Activity.this);
		Intent i = new Intent(getApplicationContext(), MainActivityTab.class);
		i.putExtra("facebookId",facebookId);
		i.putExtra("facebookName", facebookName);
		i.putExtra("facebookEmail", facebookEmail);
		startActivity(i);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
