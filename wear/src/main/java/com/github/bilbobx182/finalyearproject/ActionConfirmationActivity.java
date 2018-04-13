package com.github.bilbobx182.finalyearproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wear.widget.CircularProgressLayout;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;

public class ActionConfirmationActivity extends Activity implements
        CircularProgressLayout.OnTimerFinishedListener, View.OnClickListener {


    private CircularProgressLayout circularProgressLayout;
    private String spokenText = "";
    String clearCommand = "";

    private TextView headerText;
    private TextView subText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_confirmation);

        headerText = findViewById(R.id.confirmSendingMainText);
        subText = findViewById(R.id.confirmSubText);

        if (getIntent().getExtras().getString("spokenText") != null) {
            spokenText = getIntent().getExtras().getString("spokenText");
            setWarningText("Sending message", "working on it now!");
        }

        if (getIntent().getExtras().getString("clear") != null) {
            clearCommand = getIntent().getExtras().getString("clear");
            spokenText = "@@clear";

            setWarningText("Clearing now", "scrubbing without the soap!");
        }

        /*
        REFERENCE Android wear confirmation doccumentation
        https://developer.android.com/training/wearables/ui/confirm.html
        Last Accessed : 13/April/2018
         */
        circularProgressLayout = findViewById(R.id.circular_progress);
        circularProgressLayout.setOnTimerFinishedListener(this);
        circularProgressLayout.setOnClickListener(this);

        circularProgressLayout.setTotalTime(2500);
        circularProgressLayout.startTimer();

    }

    @Override
    public void onTimerFinished(CircularProgressLayout layout) {
        sendMessage();
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, "Sent!");
        startActivity(intent);
        finish();
    }
    // End Reference

    @Override
    public void onClick(View view) {
        if (view.equals(circularProgressLayout)) {
            circularProgressLayout.stopTimer();
            displayToast();
            finish();
        }
    }

    private void sendMessage() {
        HashMap<String, String> messageValues = new HashMap<>();
        messageValues.put("queueurl", getQueue());
        messageValues.put("message", spokenText);
        messageValues.put("location", "1,1");

        RequestPerformer requestPerformer = new RequestPerformer();
        requestPerformer.performSendMessage(messageValues);
    }

    private String getQueue() {
        try {
            WatchDBManager watchDBManager = new WatchDBManager(getApplicationContext());
            watchDBManager.open();
            return watchDBManager.getQueueURL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ("NOOOO!");
    }

    private void setWarningText(String header, String sub) {
        headerText.setText(header);
        subText.setText(sub);
    }

    private void displayToast() {
        if (spokenText.equals("clear")) {
            Toast.makeText(this, "Didn't clear!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sending canceled!", Toast.LENGTH_SHORT).show();
        }
    }
}
