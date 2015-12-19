package ua.com.lokaso.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import ua.com.lokaso.android.app.App;
import ua.com.lokaso.android.common.ActivityBase;
import ua.com.lokaso.android.constants.Constants;
import ua.com.lokaso.android.model.Locations;
import ua.com.lokaso.android.util.CustomRequest;
import ua.com.lokaso.android.util.Helper;

/**
 * Created by Amit on 12/13/2015.
 */
public class UserPicUpload extends ActivityBase {
    ImageView selectedImage;
    EditText postAskTitle, post_ask_detail;
    Button uploadPicNow;
    Bitmap bitmapImagePhoto;
    ByteArrayOutputStream byteArrayOutputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_pic);
        postAskTitle = (EditText) findViewById(R.id.post_ask_category);
        post_ask_detail = (EditText) findViewById(R.id.post_ask_detail);
        uploadPicNow = (Button) findViewById(R.id.post_ask_submit);
        uploadPicNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = postAskTitle.getText().toString();
                if(title.isEmpty()){
                    Helper.showAlertResponse(UserPicUpload.this,"Please assign any title.",false,true);
                }else{
                   if(App.getInstance().isConnected()) {
                       uploadDetails();
                   }else{
                       Helper.showAlertResponse(UserPicUpload.this,"Please check your Internet connection.",false,true);
                   }
                }

            }
        });

        selectedImage = (ImageView)findViewById(R.id.select_upload_image);
        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(UserPicUpload.this);

                builderSingle.setTitle("Select a profile picture");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserPicUpload.this,
                        android.R.layout.select_dialog_singlechoice);

                arrayAdapter.add("Import from facebook");
                arrayAdapter.add("Take photo");
                arrayAdapter.add("Choose from library");
                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                if ("Take photo".equalsIgnoreCase(strName)) {
                                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, Constants.CAMERA_PIC_REQUEST);
                                } else if ("Choose from library".equalsIgnoreCase(strName)) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select file to upload "), Constants.SELECT_FILE1);
                                }

                            }
                        });
                builderSingle.show();

            }
        });
        LocalBroadcastManager.getInstance(UserPicUpload.this).registerReceiver(mUpdateImageReceiver, new IntentFilter(Constants.IMAGE_RECEIVED));
    }

    public void uploadDetails(){
        showpDialog();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_USERS_PIC_UPLOAD, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidepDialog();
                        //ActivityCompat.finishAffinity(UserPicUpload.this);
                        UserPicUpload.this.finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",String.valueOf(App.getInstance().getId()));
                params.put("title",postAskTitle.getText().toString());
                params.put("description",post_ask_detail.getText().toString());
                params.put("file_data",convertBitmapImageToString(bitmapImagePhoto));
                return params;
            }
        };

        App.getInstance().addToRequestQueue(jsonReq);
    }

    private BroadcastReceiver mUpdateImageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            int w = selectedImage.getWidth();
            int h = selectedImage.getHeight();
            bitmapImagePhoto = (Bitmap) b.get("image");
            selectedImage.setImageBitmap(bitmapImagePhoto);
            selectedImage.setLayoutParams(new RelativeLayout.LayoutParams(w, h));
            //bundle.putAll(b); // Might be erroneous;

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {

            case Constants.CAMERA_PIC_REQUEST:
                try
                {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    //storeBitmapImage(thumbnail);
                    Intent intent = new Intent(Constants.IMAGE_RECEIVED);
                    intent.putExtra("image", thumbnail);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }
                catch(Exception ex)
                {
                    Helper.showAlertResponse(UserPicUpload.this, "Image not received. Please try again!", false, false);
                    Log.e(this.getClass().getName(), "Amit error[" + ex.getMessage() + "]");
                }
                break;
            case Constants.SELECT_FILE1:
                Log.v(this.getClass().getName(),"Amit image gallary["+data+"]" );
                try {
                    InputStream stream = getContentResolver().openInputStream(data.getData());

                    Bitmap bitmap = BitmapFactory.decodeStream(stream);//decodeSampledBitmapFromResourceMemOpt(stream, 100, 100);//BitmapFactory.decodeStream(stream);
                    stream.close();

                    Intent intent1 = new Intent(Constants.IMAGE_RECEIVED);
                    intent1.putExtra("image", bitmap);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
                } catch (Exception ex){
                    Log.e(this.getClass().getName(), "File not found ["+ex.getMessage()+"]");
                    Helper.showAlertResponse(UserPicUpload.this, "Unable to fetch the image. Please try again!", false, false);
                }
                break;

            default:
                Log.v("SignupActivity", "Amit galery ["+data+"]");
        }


    }
    public Bitmap decodeSampledBitmapFromResourceMemOpt(InputStream inputStream, int reqWidth, int reqHeight) {

        byte[] byteArr = new byte[0];
        byte[] buffer = new byte[1024];
        int len;
        int count = 0;

        try {
            while ((len = inputStream.read(buffer)) > -1) {
                if (len != 0) {
                    if (count + len > byteArr.length) {
                        byte[] newbuf = new byte[(count + len) * 2];
                        System.arraycopy(byteArr, 0, newbuf, 0, count);
                        byteArr = newbuf;
                    }

                    System.arraycopy(buffer, 0, byteArr, count, len);
                    count += len;
                }
            }

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArr, 0, count, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth,	reqHeight);
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            int[] pids = { android.os.Process.myPid() };
            //MemoryInfo myMemInfo = mAM.getProcessMemoryInfo(pids)[0];
            //Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss);

            return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
    public int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }


        return inSampleSize;

    }

    private String convertBitmapImageToString(Bitmap bitmap){
        if(bitmap != null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }else{
            return "";
        }
    }

    private void cleanImageData(){
        if(byteArrayOutputStream!=null) {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bitmapImagePhoto!=null) {
            bitmapImagePhoto = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanImageData();
    }
}
