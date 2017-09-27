package com.github.bilbobx182.mirrorm1rr0r;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Log.d("Hashed", result);
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
}
