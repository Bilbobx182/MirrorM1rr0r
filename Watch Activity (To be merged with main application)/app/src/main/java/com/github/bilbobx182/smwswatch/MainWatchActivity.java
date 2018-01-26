package com.github.bilbobx182.smwswatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

public class MainWatchActivity extends WearableActivity {

    private Button clearMirror;
    private Button sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_watch);
        // Enables Always-on
        setAmbientEnabled();

        setupClearMirror();
        setupSendMessage();
    }

    private void setupSendMessage() {
        sendMessageButton = (Button) findViewById(R.id.sendMirrorMessage);
        sendMessageButton.setOnClickListener(v -> {


            Intent intent = new Intent(this, StartSpeechActivity.class);
            startActivity(intent);

        });
    }

    private void setupClearMirror() {
        clearMirror = (Button) findViewById(R.id.clearMirrorButton);
        clearMirror.setOnClickListener(v -> {

            String baseURL = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/" +
                    "sendfifomessage?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo" +
                    "&message=" + "/clear";

            HTTPAsyncRequest asyncRequest = new HTTPAsyncRequest();
            asyncRequest.execute(baseURL);
            String result = "";
            try {
                result = asyncRequest.get().toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d("MainWatchActivity", result);

        });
    }
}
