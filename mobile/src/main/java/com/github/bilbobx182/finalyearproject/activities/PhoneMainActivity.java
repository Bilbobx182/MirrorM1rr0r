package com.github.bilbobx182.finalyearproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.bilbobx182.finalyearproject.R;

public class PhoneMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_main);
        Intent openNavMenu = new Intent(this.getApplicationContext(),NavDrawerSMWS.class);
        startActivity(openNavMenu);
    }
}
