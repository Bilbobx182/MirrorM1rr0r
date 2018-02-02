package com.github.bilbobx182.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.util.List;

public class SendVoiceMessageWear extends WearableActivity {

    private TextView sendMessageTextView;
    private static final int speechRequestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_voice_message_wear);

        sendMessageTextView = findViewById(R.id.text);

        setAmbientEnabled();
        displaySpeechRecognizer();
    }

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, speechRequestCode);
        //ToDo Link with main code for AsyncSend and send the Message
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == speechRequestCode && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            //ToDo Display Spoken Text to them to inform them of it.
            sendMessageTextView.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
