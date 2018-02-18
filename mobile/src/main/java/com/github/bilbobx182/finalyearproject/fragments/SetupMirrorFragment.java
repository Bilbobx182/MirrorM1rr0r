package com.github.bilbobx182.finalyearproject.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.bilbobx182.finalyearproject.R;

public class SetupMirrorFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static Button detectButton;
    private static TextView setupFirstInstruction;
    private static TextView setupSecondInstruction;
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
        setWidgets();
    }
    

    private void setWidgets() {
        detectButton = getActivity().findViewById(R.id.setupInstructionDetect);
        detectButton.setOnClickListener(this);

        setupFirstInstruction = getActivity().findViewById(R.id.setupInstruction1);
        setupSecondInstruction = getActivity().findViewById(R.id.setupInstruction2);
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
                    // wifiConnection.getExtraInfo();
                    Log.d("WifiTrue", wifiConnection.getExtraInfo());
                   updateWidgets(wifiConnection.getExtraInfo());
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
        setupFirstInstruction.setText("Network : " +SSID);
        setupSecondInstruction.setText("Please enter network password for mirror");
        detectButton.setText("Submit!");
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
