package com.github.bilbobx182.mirrorm1rr0r;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView queryResult;
    Spinner ySpinner;
    Spinner xSpinner;
    EditText queryInputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doneButton = (Button) findViewById(R.id.commitButton);
        queryResult = (TextView) findViewById(R.id.queryResponseTextView);
        Intent ad = new Intent(this, SMWSMenu.class);
        startActivity(ad);
        setupSpinners();

        doneButton.setOnClickListener(v -> {
            processDoneButtonActions();
        });
    }

    private void processDoneButtonActions() {
        beginMessageTransformation();
    }

    private void beginMessageTransformation() {
        populateDatabaseWithMessage();
        parseBeforeSendMessage();
    }

    private void populateDatabaseWithMessage() {
        DBManager db = new DBManager(getApplicationContext());
        try {
            db.open();
            boolean result = db.insertValue(UUID.randomUUID().toString());
            db.close();
            Log.d("SendMessageActivity", String.valueOf(result));
        } catch (Exception ex) {
            Log.d("SendMessageActivity", "Failure");
        }
    }

    private void parseBeforeSendMessage() {
        queryInputEditText = (EditText) findViewById(R.id.queryEditText);
        String input = queryInputEditText.getText().toString();

        queryResult.setText("Working on sending your contents !");

        String baseURL = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/" +
                "sendfifomessage?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo" +
                "&message=" + input;
        sendMessage(baseURL);
    }

    private void sendMessage(String baseURL) {

        String[] spinnerValues = getSpinnerValue();
        baseURL = baseURL + "&location=" + String.valueOf(spinnerValues[0]) + "," + String.valueOf(spinnerValues[1]);
        HTTPAsyncRequest thread = new HTTPAsyncRequest();
        thread.execute(baseURL);
        try {
            String result = "";
            result = thread.get().toString();
            queryResult.setText(result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void setupSpinners() {
        ySpinner = (Spinner) findViewById(R.id.ySpinner);
        ArrayAdapter<CharSequence> yAdapter = ArrayAdapter.createFromResource(this,
                R.array.yPos, android.R.layout.simple_spinner_item);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(yAdapter);

        xSpinner = (Spinner) findViewById(R.id.xSpinner);
        ArrayAdapter<CharSequence> xAdapter = ArrayAdapter.createFromResource(this,
                R.array.xPos, android.R.layout.simple_spinner_item);
        xAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xSpinner.setAdapter(xAdapter);
    }


    private String[] getSpinnerValue() {
        String xSpinnerValue = xSpinner.getSelectedItem().toString();
        String ySpinnerValue = ySpinner.getSelectedItem().toString();
        if (ySpinnerValue != null && xSpinnerValue != null) {
            return computeSpinners(xSpinnerValue, ySpinnerValue);
        } else {
            String[] results = new String[2];
            results[0] = "1";
            results[1] = "1";
            return results;
        }
    }

    private String[] computeSpinners(String xSpinnerValue, String ySpinnerValue) {

        String[] yArray = {"Top", "Center", "Bottom"};
        String[] xArray = {"Left", "Center", "Right"};
        String[] results = new String[2];

        for (int yLocation = 0; yLocation < yArray.length; yLocation++) {
            if (yArray[yLocation].contains(ySpinnerValue)) {
                results[0] = String.valueOf(yLocation);
            }
        }
        for (int xLocation = 0; xLocation < yArray.length; xLocation++) {
            if (xArray[xLocation].contains(xSpinnerValue)) {
                results[1] = String.valueOf(xLocation);
            }
        }
        return results;
    }

}
