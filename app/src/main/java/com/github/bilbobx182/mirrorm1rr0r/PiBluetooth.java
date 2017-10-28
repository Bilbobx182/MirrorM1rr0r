package com.github.bilbobx182.mirrorm1rr0r;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import rx.Subscription;


public class PiBluetooth extends AppCompatActivity {
    Subscription scanSubscription;
    RxBleClient rxBleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pi_bluetooth);
        rxBleClient = RxBleClient.create(this);

        scanSubscription = rxBleClient.scanBleDevices(
                new ScanSettings.Builder().build())
                .subscribe(
                        scanResult -> {
                            connectToPi(scanResult);
                        },
                        throwable -> {
                            Log.d("CIARANTEST", "BLEBROKE");
                            // Handle an error here.
                        }
                );
    }

    private void connectToPi(ScanResult scanResult) {
        Subscription subscription = scanResult.getBleDevice().establishConnection(PiBluetooth.this, true)
                .subscribe(rxBleConnection -> {
                    //TODO Make it so it checks that it's connecting and sending to the PI
                    scanResult.getBleDevice().establishConnection(PiBluetooth.this, false);
                    //TODO the sending logic properly implemented
//                            .flatMap(rxBleCon -> rxBleConnection.writeCharacteristic('characteristicUUID', 'bytesToWrite'))
//                            .subscribe(characteristicValue -> {
//                                // Characteristic value confirmed.
//                            });
                });
        Log.d("CIARANTEST", scanResult.toString());
    }

    /*
    //TODO Determine if I actually need this code.
    // ScanSubscription
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                         scanSubscription.unsubscribe();
     */
}