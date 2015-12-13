package ua.com.lokaso.android.util;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ua.com.lokaso.android.R;

public class Helper extends Application {

    static boolean shouldClose = false;

    static Context context;

    public boolean isValidEmail(String email) {

    	if (TextUtils.isEmpty(email)) {

    		return false;

    	} else {

    		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    	}
    }

    static Dialog mWarnDialog;
    public static void showAlertResponse(final Context context, final String msg, boolean shouldClose, final boolean cancelable)
    {

        Helper.shouldClose = shouldClose;
        Helper.context = context;

        ((Activity) Helper.context).runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                mWarnDialog = new Dialog(context);
                mWarnDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mWarnDialog.setContentView(R.layout.common_dialog_layout);
                mWarnDialog.setCanceledOnTouchOutside(false);
                TextView msgTV = (TextView) mWarnDialog.findViewById(R.id.message);
                mWarnDialog.setCancelable(cancelable);
                //mWarnDialog.setPositiveButton(true);
                //mWarnDialog.setNegativeButton(false);
                msgTV.setText(msg);
                LinearLayout okLayout = (LinearLayout)mWarnDialog.findViewById(R.id.singleButton);
                okLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mWarnDialog.dismiss();
                        if (Helper.shouldClose) {
                            ((Activity) Helper.context).finish();
                        }

                    }
                });

                mWarnDialog.show();

            }
        });

    }


    public boolean isValidLogin(String login) {

        String regExpn = "^([a-zA-Z]{4,24})?([a-zA-Z][a-zA-Z0-9_]{4,24})$";
        CharSequence inputStr = login;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }

    public boolean isValidSearchQuery(String query) {

        String regExpn = "^([a-zA-Z]{1,24})?([a-zA-Z][a-zA-Z0-9_]{1,24})$";
        CharSequence inputStr = query;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }
    
    public boolean isValidPassword(String password) {

        String regExpn = "^[a-z0-9_]{6,24}$";
        CharSequence inputStr = password;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {

            return true;

        } else {

            return false;
        }
    }
}
