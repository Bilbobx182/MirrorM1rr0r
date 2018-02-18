package com.github.bilbobx182.finalyearproject;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPAsyncRequest extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamDataReader = new InputStreamReader(inputStream);

            int data = inputStreamDataReader.read();
            while (data != -1) {
                char current = (char) data;
                data = inputStreamDataReader.read();
                result += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return result;
    }
}
