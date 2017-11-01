package com.github.bilbobx182.mirrorm1rr0r;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.UUID;

import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    Subscription scanSubscription;
    RxBleClient rxBleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doneButton = (Button) findViewById(R.id.commitButton);
        if (!isCoarse()) {
            createRequestDialogue();
        }
        doneButton.setOnClickListener(v -> {
            testPiMethod();
        });
    }

    private void testPiMethod() {
        rxBleClient = RxBleClient.create(this);
        TextView bluetooth = (TextView) findViewById(R.id.bluetooth);
        scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder().build()).subscribe(
                scanResult -> {
                    if (scanResult.getBleDevice().getName() != null && scanResult.getBleDevice().getName().toString().equals("test")) {
                        doWrite(scanResult);
                        scanSubscription.unsubscribe();
                    }
                },
                throwable -> {
                    Log.d("CIARANTEST", "BLEBROKE");
                    // Handle an error here.
                }
        );
    }

    private void doWrite(ScanResult scanResult) {
        String messageString = "I am so close";
        byte[] message = messageString.getBytes();
        final UUID writeID = UUID.fromString("ffffffff-ffff-ffff-ffff-fffffffff0");
        Subscription subscription = scanResult.getBleDevice().establishConnection(true).subscribe(rxBleConnection -> {
            rxBleConnection.writeCharacteristic(writeID, message);
        });
        //TODO I AM CLOSING THE CONNECTIONS TOO FAST. PUT THIS MAYBE THE METHOD THAT CALLS IT INTO IT'S OWN THREAD / ASYNC SO IT CAN DO IT'S THANG
        // THAT WAY IT WILL WRITE THEN CLOSE
        subscription.unsubscribe();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Handle the case where we cant access location
            Log.d("Ciaran", "SHITT");
        }
    }

    private void createRequestDialogue() {
        AlertDialog.Builder permissionPopup = new AlertDialog.Builder(MainActivity.this);
        permissionPopup.setMessage("We need them perms");

        permissionPopup.setPositiveButton("Yes",
                (dialog, id) -> {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                });

        permissionPopup.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = permissionPopup.create();
        alert11.show();

    }

    // "Sand is Coarse, Not like your skin Padame"
    private boolean isCoarse() {
        return getApplicationContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, android.os.Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }


/*
   scanResult.getBleDevice().establishConnection(MainActivity.this, false)
                                            .flatMap(rxBleCon -> rxBleConnection.writeCharacteristic(, "test".getBytes()))
                                            .subscribe(characteristicValue -> {
                                                // Characteristic value confirmed.
                                            });
 */
//    private String validateInput() {
//        EditText username = (EditText) findViewById(R.id.nameEditText);
//        EditText rawPassword = (EditText) findViewById(R.id.passwordEditText);
//
//        //// TODO: 27/09/2017 Add a check to see if the username is already there. But I suppose this is the login Screen.
//        if (username.getText() != null && rawPassword.getText() != null) {
//            String toHash = username.getText() + "" + rawPassword.getText();
//            Encryptor encryptor = new Encryptor();
//            return encryptor.hashCode(toHash);
//
//        } else {
//            return "Woops empty fields";
//        }
//    }
}
