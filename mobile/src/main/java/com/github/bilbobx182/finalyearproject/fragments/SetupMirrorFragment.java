package com.github.bilbobx182.finalyearproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.sharedcode.RequestPerformer;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.sql.SQLException;
import java.util.UUID;

import rx.Subscription;

public class SetupMirrorFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static Button detectButton;
    private static TextView setupFirstInstruction;
    private static TextView setupSecondInstruction;
    private static EditText wifiPassEditText;
    private static DBManager dbManager;
    private static RequestPerformer requestPerformer;
    private static String wifiSSID;
    private static String wifiPass;

    private int setupCount = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SetupMirrorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SetupMirrorFragment newInstance(String param1, String param2) {
        SetupMirrorFragment fragment = new SetupMirrorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup_mirror, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbManager = new DBManager(getActivity());
        requestPerformer = new RequestPerformer();
        if (!isCoarse()) {
            createRequestDialogue();
        }
        setWidgets();
    }


    private void setWidgets() {
        detectButton = getActivity().findViewById(R.id.setupInstructionDetect);
        detectButton.setOnClickListener(this);

        setupFirstInstruction = getActivity().findViewById(R.id.setupInstruction1);
        setupSecondInstruction = getActivity().findViewById(R.id.setupInstruction2);
        wifiPassEditText = getActivity().findViewById(R.id.wifiEntry);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.setupInstructionDetect): {
                NetworkInfo wifiConnection = getWifiInformation();

                if (wifiConnection.isConnected()) {
                    updateWidgets(wifiConnection.getExtraInfo());
                    if (setupCount >= 1) {
                        wifiPass = wifiPassEditText.getText().toString();
                        createQueueSetup();
                        testPiMethod();

                        //TODO IF THE BLE STUFF DOESN'T SEND WITH THIS ENABLED IT COULD BE BECAUSE I AM EXITING TOO QUICKLY
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                    }
                    setupCount++;

                } else {
                    // ToDo Reprimand the bad user, with a friendly message of course.

                }

            }
        }
    }

    private NetworkInfo getWifiInformation() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connectionInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return connectionInfo;

    }

    private void updateWidgets(String SSID) {
        setupFirstInstruction.setText("Network : " + SSID);
        wifiSSID = SSID;
        setupSecondInstruction.setText("Please enter network password for mirror");
        detectButton.setText("Submit!");
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    Subscription scanSubscription;
    RxBleClient rxBleClient;

    // I think the client may be sending them too quickly
    private void testPiMethod() {
           /*
        Reference: RxAndroidBLE documentation
        https://www.polidea.com/blog/RxAndroidBLE_the_most_Simple_way_to_code_Bluetooth_Low_Energy_devices/
        Last Accessed: 9/November/2017
         */
        rxBleClient = RxBleClient.create(getContext());
        scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder().build()).subscribe(
                scanResult -> {
                    // End Reference
                    if (scanResult.getBleDevice().getName() != null && scanResult.getBleDevice().getName().toString().equals("SMWS")) {
                        doWrite(scanResult);
                        scanSubscription.unsubscribe();
                    }
                },
                throwable -> {
                    Log.d("CIARANTEST", "BLEBROKE");
                }
        );
    }

    private void doWrite(ScanResult scanResult) {
        String messageString = "{\n" +
                "\"queue\" : \"" + dbManager.getUserInformationByColumn("queue") + "\",\n" +
                "\"WiFi\" : \"" + wifiSSID + "\",\n" +
                "\"Pass\" : \"" + wifiPass + "\"\n" +
                "}";
        byte[] message = messageString.getBytes();
        final UUID writeID = UUID.fromString("ffffffff-ffff-ffff-ffff-fffffffffff4");
        RxBleDevice device = scanResult.getBleDevice();


        /*
        Reference: RxAndroidBLE documentation
        https://www.polidea.com/blog/RxAndroidBLE_the_most_Simple_way_to_code_Bluetooth_Low_Energy_devices/
        Last Accessed: 9/November/2017
         */

        device.establishConnection(true)
                .flatMap(rxBleConnection -> rxBleConnection.createNewLongWriteBuilder()
                        .setCharacteristicUuid(writeID).setBytes(message).build()
                )
                .subscribe(
                        byteArray -> Log.d("SetupMirror", "Content Written"),
                        throwable -> Toast.makeText(getContext(), "Sending message to mirror failed", Toast.LENGTH_SHORT).show()
                );
        // End Reference
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Handle the case where we cant access location
            Log.d("Ciaran", "Oh God, something went wrong");
        }
    }

    private void createRequestDialogue() {
        AlertDialog.Builder permissionPopup = new AlertDialog.Builder(getContext());
        permissionPopup.setMessage("Permissions are needed for the application");

        permissionPopup.setPositiveButton("Yes",
                (dialog, id) -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE}, 1));

        permissionPopup.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = permissionPopup.create();
        alert11.show();

    }

    private void createQueueSetup() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            createRequestDialogue();
        }

        String IMEI = telephonyManager.getDeviceId();
        if (IMEI == null) {
            // Because on some rooted devices they mess up. Also enables tablet use.
            // Long java line. 36 = UUID length, 15 == IMEI length. We just shorten to standardise.
            IMEI = UUID.randomUUID().toString().substring(0, Math.min(36, 15));
        }
        String firstname = "";
        String surname = "";
        DBManager dbManager = new DBManager(getActivity());
        try {
            dbManager.open();
            firstname = dbManager.getUserInformationByColumn("firstname");
            surname = dbManager.getUserInformationByColumn("surname");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String uniqueUserCharacteristics = IMEI + firstname + surname;
        String queue = requestPerformer.createQueue(Integer.toHexString(uniqueUserCharacteristics.hashCode()));
        dbManager.updateUserInformation("queue", queue);
    }

    private boolean isCoarse() {
        return getContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, android.os.Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }
}
