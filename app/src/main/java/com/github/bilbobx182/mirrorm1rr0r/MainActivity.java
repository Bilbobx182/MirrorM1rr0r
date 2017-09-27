package com.github.bilbobx182.mirrorm1rr0r;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doneButton = (Button) findViewById(R.id.commitButton);
        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String result = validateInput();
                if (!result.equals("ERROR: 1337")) {
                    Log.d("Hashed", result);
                    new CreateAWSQueue().execute(result);
                }
            }
        });
    }

    private String validateInput() {
        EditText username = (EditText) findViewById(R.id.nameEditText);
        EditText rawPassword = (EditText) findViewById(R.id.passwordEditText);

        //// TODO: 27/09/2017 Add a check to see if the username is already there. But I suppose this is the login Screen.
        if (username.getText() != null && rawPassword.getText() != null) {
            String toHash = username.getText() + "" + rawPassword.getText();
            Encryptor encryptor = new Encryptor();
            return encryptor.hashCode(toHash);

        } else {
            return "Woops empty fields";
        }
    }

    private class CreateAWSQueue extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] hashedTopic) {
            AWS aws = new AWS();
            aws.createQueue(hashedTopic[0]);
            return "Done";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }
}
