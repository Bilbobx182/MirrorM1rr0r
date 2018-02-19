package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class SendVoiceMessageWear extends WearableActivity {

    private TextView sendMessageTextView;
    private static final int speechRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_voice_message_wear);

        sendMessageTextView = findViewById(R.id.confirmSendingMainText);

        setAmbientEnabled();
        displaySpeechRecognizer();
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
            sendMessageTextView.setText(spokenText);
            sendMessage(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    private void sendMessage(String spokenText) {

        Intent intent = new Intent(this, ActionConfirmationActivity.class);
        intent.putExtra("message",spokenText);
        startActivity(intent);
    }
}
