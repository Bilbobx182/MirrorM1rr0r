package com.github.bilbobx182.mirrorm1rr0r;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

        initiateDatabase();


        Button doneButton = (Button) findViewById(R.id.commitButton);
        queryResult = (TextView) findViewById(R.id.queryResponseTextView);

        setupSpinners();

        doneButton.setOnClickListener(v -> {
          processDoneButtonActions();
        });
    }

    private void initiateDatabase() {
        DBManager db=new DBManager(getApplicationContext());
        try
        {
            db.open();
         //   db.insertMessageToDatabase("TEST VALUE2");
            Cursor getStuff = db.getAll();
            if (getStuff.moveToFirst()){
                do{
                    String data = getStuff.getString(getStuff.getColumnIndex("data"));
                    // do what ever you want here
                    Log.d("HELLO0",data);
                }while(getStuff.moveToNext());
            }
            getStuff.close();
            System.out.println("HELLO");
        }
        catch (Exception ex)
        {
            Log.d("MAINACTIVITY.JAVA","Something wrong happened trying to open the DB");
        }
    }

    private void processDoneButtonActions() {
        queryInputEditText = (EditText) findViewById(R.id.queryEditText);
        String input = queryInputEditText.getText().toString();

        queryResult.setText("Working on sending your contents !");

        String baseURL = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/" +
                "sendfifomessage?queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo" +
                "&message=" + input;
        sendMessage(baseURL);
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

    private void sendMessage(String baseURL) {

        int yNum = 1;
        int xNum = 1;

        String xSpinnerValue = xSpinner.getSelectedItem().toString();
        String ySpinnerValue = ySpinner.getSelectedItem().toString();

        if (ySpinnerValue != null && xSpinnerValue != null) {
            String[] yArray = {"Top", "Center", "Bottom"};
            String[] xArray = {"Left", "Center", "Right"};

            for (int yLocation = 0; yLocation < yArray.length; yLocation++) {
                if (yArray[yLocation].contains(ySpinnerValue)) {
                    yNum = yLocation;
                }
            }
            for (int xLocation = 0; xLocation < yArray.length; xLocation++) {
                if (xArray[xLocation].contains(xSpinnerValue)) {
                    xNum = xLocation;
                }
            }
        }

        baseURL = baseURL + "&location=" + String.valueOf(yNum) + "," + String.valueOf(xNum);
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
}
