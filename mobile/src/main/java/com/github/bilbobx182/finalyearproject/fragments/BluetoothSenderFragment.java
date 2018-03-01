package com.github.bilbobx182.finalyearproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.github.bilbobx182.finalyearproject.R;
import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.UUID;

import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BluetoothSenderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BluetoothSenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BluetoothSenderFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BluetoothSenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BluetoothSenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BluetoothSenderFragment newInstance(String param1, String param2) {
        BluetoothSenderFragment fragment = new BluetoothSenderFragment();
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
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if (!isCoarse()) {
            createRequestDialogue();

        }
        testPiMethod();
    }

    Subscription scanSubscription;
    RxBleClient rxBleClient;
    private void testPiMethod() {
           /*
        Reference: RxAndroidBLE documentation
        https://www.polidea.com/blog/RxAndroidBLE_the_most_Simple_way_to_code_Bluetooth_Low_Energy_devices/
        Last Accessed: 9/November/2017
         */
        rxBleClient = RxBleClient.create(getContext());
        // TextView bluetooth = (TextView) findViewById(R.id.bluetooth);
        scanSubscription = rxBleClient.scanBleDevices(new ScanSettings.Builder().build()).subscribe(
                scanResult -> {
                    // End Reference
                    if(scanResult.getBleDevice().getName() != null) {
                        if (scanResult.getBleDevice().getName().toString().equals("test")) {
                            doWrite(scanResult);
                            scanSubscription.unsubscribe();
                        }
                    }
                },
                throwable -> {
                    Log.d("CIARANTEST", "BLEBROKE");
                }
        );
    }

    private void doWrite(ScanResult scanResult) {
        String messageString = "{" +
                " \'SSID\' : \'" + "IsThisTheKrustyKrab" +"\'," +
                " \'QUEUE\' : \'" + "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo" +"\'," +
                " \'PASS\' : \'" + "N0thisispatrick" +"\'" +
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
            Log.d("Ciaran", "Oh God, something went wrong");
        }
    }

    private void createRequestDialogue() {
        AlertDialog.Builder permissionPopup = new AlertDialog.Builder(getContext());
        permissionPopup.setMessage("Permissions are needed for the application");

        permissionPopup.setPositiveButton("Yes",
                (dialog, id) -> requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1));

        permissionPopup.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = permissionPopup.create();
        alert11.show();

    }

    private boolean isCoarse() {
        return getContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, android.os.Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }











    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bluetooth_sender, container, false);
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

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
