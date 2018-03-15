package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends WearableActivity implements MessageClient.OnMessageReceivedListener {

    ImageButton sendMessageButton;
    ImageButton clearMirrorButton;
    private static final int speechRequestCode = 0;

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
                        displaySpeechRecognizer();

                    } else {
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
                sendMessage("clear", "^/^clear");
            }
        });
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String queueURL = new String(messageEvent.getData());
        WatchDBManager watchDBManager = new WatchDBManager(this);
        Toast.makeText(this, "Phone message recieved!", Toast.LENGTH_SHORT).show();

        try {
            watchDBManager.open();
            watchDBManager.updateQueueURL(queueURL);
            watchDBManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, speechRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == speechRequestCode && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            sendMessage("spokenText", spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendMessage(String key, String messageToSend) {

        Intent intent = new Intent(this, ActionConfirmationActivity.class);
        intent.putExtra(key, messageToSend);
        startActivity(intent);
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

