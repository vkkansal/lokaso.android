package ua.com.qascript.android.util;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kishore.chauhan on 12/6/2015.
 */
public class HttpConnectionClass {
    /**
     * // Given a URL, establishes an HttpUrlConnection and retrieves
     // the web page content as a InputStream, which it returns as
     // a string.
     * @param myurl
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String myurl, String method, String uploadString) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            if(uploadString != null){
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(uploadString);
                wr.flush();
                wr.close();
            }
            int response = conn.getResponseCode();
            Log.d("HttpConnectionClass", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

}
