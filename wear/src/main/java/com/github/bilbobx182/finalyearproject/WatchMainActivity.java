package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

public class WatchMainActivity extends WearableActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_main);

        mTextView = (TextView) findViewById(R.id.confirmSendingMainText);

        // This is the launch Activity for the watch so I can add icons etc if I want to.
        // ToDo Add animations
        setAmbientEnabled();

        Intent startMainWatchActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(startMainWatchActivity);
    }
}
