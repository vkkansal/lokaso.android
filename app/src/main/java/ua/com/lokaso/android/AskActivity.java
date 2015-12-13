package ua.com.lokaso.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.common.ActivityBase;
import ua.com.lokaso.android.util.CustomRequest;


public class AskActivity extends ActivityBase {

    Toolbar toolbar;

    EditText askEdit;
    CheckBox askCheckBox;

    long profileId, fromUserId = 0;
    int anonymousQuestions;
    String questionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_detail);

        //        Инициализируем Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        profileId = i.getLongExtra("profileId", 0);
        anonymousQuestions = i.getIntExtra("anonymousQuestions", 1);

        if (anonymousQuestions != 1) {

            fromUserId = App.getInstance().getId();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ask, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_ask: {

                questionText = askEdit.getText().toString();
                questionText = questionText.trim();

                if (questionText.length() > 0) {

                    if (App.getInstance().isConnected()) {

                        sendAsk();

                    } else {

                        Toast toast= Toast.makeText(getApplicationContext(), getText(R.string.msg_network_error), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                } else {

                    Toast toast= Toast.makeText(getApplicationContext(), getText(R.string.msg_enter_question), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                return true;
            }

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void sendAsk() {

        showpDialog();

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_QUESTIONS_ADD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getBoolean("error") == false) {


                            }

                        } catch (JSONException e) {

                            e.printStackTrace();

                        } finally {

                            askSendSuccess();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                askSendSuccess();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accountId", Long.toString(App.getInstance().getId()));
                params.put("accessToken", App.getInstance().getAccessToken());
                params.put("toUserId", Long.toString(profileId));
                params.put("fromUserId", Long.toString(fromUserId));
                params.put("questionText", questionText);

                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    public void askSendSuccess() {

        hidepDialog();
        Toast.makeText(getApplicationContext(), getText(R.string.msg_question_has_been_send), Toast.LENGTH_SHORT).show();
        finish();
    }
}
