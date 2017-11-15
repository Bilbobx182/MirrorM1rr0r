package com.github.bilbobx182.mirrorm1rr0r;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.UUID;

import rx.Subscription;

public class BluetoothLEActivity extends AppCompatActivity {

    Subscription scanSubscription;
    RxBleClient rxBleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_le);

        if (!isCoarse()) {
            createRequestDialogue();
        }
//        someButton.setOnClickListener(v -> {
//            testPiMethod();
//        });
    }

    private void testPiMethod() {
           /*
        Reference: RxAndroidBLE documentation
        https://www.polidea.com/blog/RxAndroidBLE_the_most_Simple_way_to_code_Bluetooth_Low_Energy_devices/
        Last Accessed: 9/November/2017
         */
        rxBleClient = RxBleClient.create(this);
       // TextView bluetooth = (TextView) findViewById(R.id.bluetooth);
        scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder().build()).subscribe(
                scanResult -> {
                    // End Reference
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
        String messageString = "testing2";
        byte[] message = messageString.getBytes();
        final UUID writeID = UUID.fromString("ffffffff-ffff-ffff-ffff-fffffffffff4");
        RxBleDevice device = scanResult.getBleDevice();


        /*
        Reference: RxAndroidBLE documentation
        https://www.polidea.com/blog/RxAndroidBLE_the_most_Simple_way_to_code_Bluetooth_Low_Energy_devices/
        Last Accessed: 9/November/2017
         */
        device.establishConnection(true)
                .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(writeID, message))
                .subscribe(characteristicValue -> {
                    // Characteristic value confirmed.
                    Log.d("HELLO", characteristicValue.toString());
                });

        // End Reference
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
        AlertDialog.Builder permissionPopup = new AlertDialog.Builder(BluetoothLEActivity.this);
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

}
