package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.sql.SQLException;

public class MainActivity extends WearableActivity {

    ImageButton sendMessageButton;
    ImageButton clearMirrorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        setupButtons();

        WatchDBManager watchDBManager = new WatchDBManager(this);
        try {
            watchDBManager.open();
            watchDBManager.updateQueueURL("https://sqs.eu-west-1.amazonaws.com/186314837751/queuename.fifo");
            Log.d("BEFORE", watchDBManager.getQueueURL());

            watchDBManager.updateQueueURL("https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo");
            Log.d("AFTER", watchDBManager.getQueueURL());

            watchDBManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

