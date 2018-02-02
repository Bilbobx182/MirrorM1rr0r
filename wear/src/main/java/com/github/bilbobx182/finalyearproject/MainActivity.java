package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends WearableActivity {

    ImageButton sendMessageButton;
    ImageButton clearMirrorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        setupButtons();

    }

    private void setupButtons() {
        sendMessageButton = findViewById(R.id.sendMessageButton);
        clearMirrorButton = findViewById(R.id.clearMirrorButton);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendMessageItent = new Intent(getApplicationContext(), SendVoiceMessageWear.class);
                startActivity(sendMessageItent);
            }
        });

        clearMirrorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //ToDo Create a Response to when they click the clear. Like a tick or something
//                Intent clearMirrorIntent = new Intent(getApplicationContext(), clearMirrorActivity.class);
//                startActivity(clearMirrorIntent);
            }
        });
    }
}

