package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;

public class MainActivity extends WearableActivity implements MessageClient.OnMessageReceivedListener {

    ImageButton sendMessageButton;
    ImageButton clearMirrorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        Wearable.getMessageClient(this).addListener(this);
        setupButtons();
    }

    private void setupButtons() {
        sendMessageButton = findViewById(R.id.sendMessageButton);
        clearMirrorButton = findViewById(R.id.clearMirrorButton);


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WatchDBManager watchDBManager = new WatchDBManager(getApplicationContext());
                try {
                    watchDBManager.open();
                    if (watchDBManager.isQueueURLSet()) {
                        Intent sendMessageItent = new Intent(getApplicationContext(), SendVoiceMessageWear.class);
                        startActivity(sendMessageItent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You haven't setup the phone yet!", Toast.LENGTH_SHORT).show();
                    }
                    watchDBManager.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String queueURL = new String(messageEvent.getData());
        WatchDBManager watchDBManager = new WatchDBManager(this);

        try {
            watchDBManager.open();
            watchDBManager.updateQueueURL(queueURL);
            watchDBManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getMessageClient(this).removeListener(this);
    }
}

