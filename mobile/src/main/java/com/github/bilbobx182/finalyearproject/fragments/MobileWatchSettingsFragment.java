package com.github.bilbobx182.finalyearproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutionException;


public class MobileWatchSettingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SET_MESSAGE_CAPABILITY = "setqueue";
    private static final String SET_MESSAGE_PATH = "/setqueue";
    private static String MESSAGE_TO_SEND = "Hello Bilbobx182 Made this";
    private static Button setupWatchContentButton;
    private static Context thisContext;

    private static String transcriptionNodeId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MobileWatchSettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MobileWatchSettingsFragment newInstance(String param1, String param2) {
        MobileWatchSettingsFragment fragment = new MobileWatchSettingsFragment();
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
        // Inflate the layout for this fragment;

        thisContext = getContext();
        return inflater.inflate(R.layout.fragment_mobile_watch_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupWatchContentButton = getActivity().findViewById(R.id.setupWatchConnect);
        setupWatchContentButton.setOnClickListener(this);
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
            case (R.id.setupWatchConnect): {
                beginSendMessageToWear();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void beginSendMessageToWear() {

        setQueueToSend();

        AsyncTask.execute(() -> {

                /*
                    REFERENCE: Android Doccumentation
                    URL: https://developer.android.com/training/wearables/data-layer/events.html#Listen
                    LAST ACCESSED: 18/02/2018
                */
            CapabilityInfo capabilityInfo;
            try {

                capabilityInfo = Tasks.await(
                        Wearable.getCapabilityClient(thisContext).getCapability(SET_MESSAGE_CAPABILITY, CapabilityClient.FILTER_REACHABLE));
                updateTranscriptionCapability(capabilityInfo);
                requestTranscription(MESSAGE_TO_SEND.getBytes());

                // END REFERENCE

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }


    private void setQueueToSend() {
        DBManager dbManager = new DBManager(getContext());
        try {
            dbManager.open();
            MESSAGE_TO_SEND = dbManager.getUserInformationByColumn("queue");
            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /*
    REFERENCE: Android Doccumentation
    URL: https://developer.android.com/training/wearables/data-layer/messages.html
    LAST ACCESSED: 18/02/2018
     */

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }
    // End reference.

    private void requestTranscription(final byte[] message) {

        AsyncTask.execute(() -> {
            if (transcriptionNodeId != null) {
                final Task<Integer> sendTask = Wearable.getMessageClient(thisContext).sendMessage(transcriptionNodeId, SET_MESSAGE_PATH, message);

                sendTask.addOnSuccessListener(dataItem -> Log.d("MESSAGESTATE", "SUCCESS"));
                sendTask.addOnFailureListener(dataItem -> Log.d("MESSAGESTATE", "FAILURE"));
                sendTask.addOnCompleteListener(task -> Log.d("MESSAGESTATE", "COMPLETE"));
            }
        });
    }
}
