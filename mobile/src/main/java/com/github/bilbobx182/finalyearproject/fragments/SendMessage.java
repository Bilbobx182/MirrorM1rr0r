package com.github.bilbobx182.finalyearproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.HTTPAsyncRequest;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.finalyearproject.RequestPerformer;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SendMessage extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner ySpinner;
    private Spinner xSpinner;
    private EditText queryInputEditText;
    private Button doneButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SendMessage() {
    }

    public static SendMessage newInstance(String param1, String param2) {
        SendMessage fragment = new SendMessage();
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

        View inflated = inflater.inflate(R.layout.fragment_send_message, container, false);

        ySpinner = inflated.findViewById(R.id.ySpinner);
        xSpinner = inflated.findViewById(R.id.xSpinner);

        setupSpinners();
        return inflated;
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
        doneButton = view.findViewById(R.id.commitButton);
        switch (view.getId()) {
            case (R.id.commitButton): {

                processDoneButtonActions();
                Toast.makeText(getContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void processDoneButtonActions() {
        beginMessageTransformation();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doneButton = getActivity().findViewById(R.id.commitButton);
        doneButton.setOnClickListener(this);
    }

    private void beginMessageTransformation() {
        populateDatabaseWithMessage();
        groupValuesBeforeSending();
    }

    private void populateDatabaseWithMessage() {
        DBManager db = new DBManager(getContext());
        try {
            db.open();
            boolean result = db.insertValue(UUID.randomUUID().toString());
            db.close();
            Log.d("SendMessageActivity", String.valueOf(result));
        } catch (Exception ex) {
            Log.d("SendMessageActivity", "Failure");
        }
    }

    private void groupValuesBeforeSending() {
        HashMap<String, String> messageValues = new HashMap<>();

        queryInputEditText = getView().findViewById(R.id.queryEditText);
        String input = queryInputEditText.getText().toString();
        String[] spinnerValues = getSpinnerValue();

        messageValues.put("message", input);
        messageValues.put("location", String.valueOf(spinnerValues[0]) + "," + String.valueOf(spinnerValues[1]));
        messageValues.put("queueurl", "https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo");

        /*
        ToDo:  Support the following in app once everything else is done
        location
        fontcolour
         */

        sendMessage(messageValues);
    }

    private void sendMessage(HashMap values) {
        RequestPerformer requestPerformer = new RequestPerformer();
        requestPerformer.performSendMessage(values);
    }

    private void setupSpinners() {
        //ToDo Figure out how to reference these in Strings.XML as R.Array again
        String yArray[] = {"Top", "Center", "Bottom"};
        String xArray[] = {"Left", "Center", "Right"};

        ArrayAdapter<String> yAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yArray);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(yAdapter);

        ArrayAdapter<String> xAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, xArray);
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
