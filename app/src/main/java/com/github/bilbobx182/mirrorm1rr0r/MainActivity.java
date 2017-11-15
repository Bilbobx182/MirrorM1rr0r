package com.github.bilbobx182.mirrorm1rr0r;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    String requestResponse;
    TextView queryResult;
    EditText queryInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doneButton = (Button) findViewById(R.id.commitButton);
        queryResult = (TextView) findViewById(R.id.queryResponseTextView);
        doneButton.setOnClickListener(v -> {
            queryInputEditText = (EditText) findViewById(R.id.queryEditText);
            String input = queryInputEditText.getText().toString();

            Log.v("EditText", input);

            String urlToPass = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendmessagetoqueue?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/normalQueue&message=" + input;
            BackgroundThread thread = new BackgroundThread();
            thread.execute(urlToPass);
        });
    }

    private class BackgroundThread extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();
                while (data != -1) {
                    char current = (char) data;
                    data = isw.read();
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

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            queryResult.append(result);
            super.onPostExecute(result);
        }
    }
}


//    private String validateInput() {
//        EditText username = (EditText) findViewById(R.id.nameEditText);
//        EditText rawPassword = (EditText) findViewById(R.id.passwordEditText);
//
//        //// TODO: 27/09/2017 Add a check to see if the username is already there. But I suppose this is the login Screen.
//        if (username.getText() != null && rawPassword.getText() != null) {
//            String toHash = username.getText() + "" + rawPassword.getText();
//            Encryptor encryptor = new Encryptor();
//            return encryptor.hashCode(toHash);
//
//        } else {
//            return "Woops empty fields";
//        }
//    }
