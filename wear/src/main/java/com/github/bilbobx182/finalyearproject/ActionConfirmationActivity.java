package com.github.bilbobx182.finalyearproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wear.widget.CircularProgressLayout;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;

public class ActionConfirmationActivity extends Activity implements
        CircularProgressLayout.OnTimerFinishedListener, View.OnClickListener {


    private CircularProgressLayout mCircularProgress;
    private String spokenText ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spokenText = getIntent().getExtras().getString("spokenText");

        setContentView(R.layout.activity_action_confirmation);

        mCircularProgress = findViewById(R.id.circular_progress);
        mCircularProgress.setOnTimerFinishedListener(this);
        mCircularProgress.setOnClickListener(this);

        mCircularProgress.setTotalTime(2500);
        mCircularProgress.startTimer();

    }

    @Override
    public void onTimerFinished(CircularProgressLayout layout) {
        sendMessage();
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE,"Sent!");
        startActivity(intent);
        //ToDo Quit and bring it back way way back to the first screen.
    }

    @Override
    public void onClick(View view) {
        if (view.equals(mCircularProgress)) {
            // User canceled, abort the action
            mCircularProgress.stopTimer();
        }
    }

    private void sendMessage() {
        HashMap<String, String> messageValues = new HashMap<>();
        messageValues.put("message", spokenText);
        messageValues.put("queueurl", getQueue());

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
        //ToDo This homie, change this.
        return("CHANGE ME LATER I NEED REAL ERROR VALIDATION!");
    }
}
