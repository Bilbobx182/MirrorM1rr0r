package com.github.bilbobx182.mirrorm1rr0r;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.internal.util.UUIDUtil_Factory;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.UUID;

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
                            Log.d("MainActivity", "FOUND :" + scanResult.getBleDevice().getName());
                        },
                        throwable -> {
                            Log.d("CIARANTEST", "BLEBROKE");
                            // Handle an error here.
                        }
                );
        scanSubscription.unsubscribe();
    }

    private void connectToPi(ScanResult scanResult) {
        UUID characteristicUuid = UUID.fromString("1");

        byte[] data = "test".getBytes();
        Subscription subscription = scanResult.getBleDevice().establishConnection(PiBluetooth.this, true)
                .subscribe(rxBleConnection -> {
                    scanResult.getBleDevice().establishConnection(PiBluetooth.this, false)
                            .flatMap(rxBleCon -> rxBleConnection.writeCharacteristic(characteristicUuid, data))
                            .subscribe(characteristicValue -> {
                                // Characteristic value confirmed.
                            });
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