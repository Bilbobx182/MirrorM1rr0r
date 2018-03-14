package com.github.bilbobx182.finalyearproject.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SendMessage extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Spinner ySpinner;
    private Spinner xSpinner;
    private Spinner textSizeSpinner;
    private EditText queryInputEditText;
    private Button doneButton;

    private ImageButton blueButton;
    private ImageButton greenButton;
    private ImageButton whiteButton;
    private ImageButton yellowButton;
    private ImageButton redButton;
    private TextView colourSelector;
    private String activeColour = "White";


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
        textSizeSpinner = inflated.findViewById(R.id.textSizeSpinner);

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
        switch (view.getId()) {
            case (R.id.commitButton): {

                processDoneButtonActions();
                Toast.makeText(getContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            case (R.id.blueColourSelector): {
                colourSelector.setText("Current Colour: Blue");
                activeColour = "Blue";
                break;
            }
            case (R.id.greenColourSelector): {
                colourSelector.setText("Current Colour: Green");
                activeColour = "Green";
                break;
            }
            case (R.id.whiteColourSelector): {
                colourSelector.setText("Current Colour: White");
                activeColour = "White";
                break;
            }
            case (R.id.yellowColourSelector): {
                colourSelector.setText("Current Colour: Yellow");
                activeColour = "Yellow";
                break;
            }
            case (R.id.redColourSelector): {
                colourSelector.setText("Current Colour: Red");
                activeColour = "Red";
                break;
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

        colourSelector = getActivity().findViewById(R.id.colourPickerHelper);

        doneButton = getActivity().findViewById(R.id.commitButton);
        doneButton.setOnClickListener(this);

        blueButton = getActivity().findViewById(R.id.blueColourSelector);
        blueButton.setOnClickListener(this);

        greenButton = getActivity().findViewById(R.id.greenColourSelector);
        greenButton.setOnClickListener(this);

        whiteButton = getActivity().findViewById(R.id.whiteColourSelector);
        whiteButton.setOnClickListener(this);

        yellowButton = getActivity().findViewById(R.id.yellowColourSelector);
        yellowButton.setOnClickListener(this);

        redButton = getActivity().findViewById(R.id.redColourSelector);
        redButton.setOnClickListener(this);

    }

    private void beginMessageTransformation() {
        groupValuesBeforeSending();
    }


    private void groupValuesBeforeSending() {
        HashMap<String, String> messageValues = new HashMap<>();

        queryInputEditText = getView().findViewById(R.id.queryEditText);
        String input = queryInputEditText.getText().toString();
        String[] spinnerValues = getSpinnerValue();

        DBManager dbManager = new DBManager(getActivity());

        messageValues.put("message", input);
        messageValues.put("location", String.valueOf(spinnerValues[0]) + "," + String.valueOf(spinnerValues[1]));
        messageValues.put("fontsize", getTextSizeSpinnerValue());
        HashMap<String, String> colourAndHex = new HashMap<>();
        colourAndHex.put("Blue", "0099cc");
        colourAndHex.put("Green", "99cc00");
        colourAndHex.put("White", "ffffff");
        colourAndHex.put("Yellow", "fdd835");
        colourAndHex.put("Red", "ff4444");

        messageValues.put("fontcolour", colourAndHex.get(activeColour));
        populateDatabaseWithMessage(messageValues);

        try {
            dbManager.open();
            messageValues.put("queueurl", dbManager.getUserInformationByColumn("queue"));
            dbManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sendMessage(messageValues);
    }

    private void populateDatabaseWithMessage(HashMap<String, String> messageValues) {
        DBManager db = new DBManager(getContext());

        try {
            db.open();
            db.insertValue(messageValues);
            db.close();

        } catch (Exception ex) {
            Log.d("SendMessageActivity", "Failure");
        }
    }

    private void sendMessage(HashMap values) {
        RequestPerformer requestPerformer = new RequestPerformer();
        requestPerformer.performSendMessage(values);
    }

    private void setupSpinners() {
        //ToDo Figure out how to reference these in Strings.XML as R.Array again
        String yArray[] = {"Top", "Center", "Bottom"};
        String xArray[] = {"Left", "Center", "Right"};
        String textSizeArray[] = {"15", "25", "35"};

        ArrayAdapter<String> yAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yArray);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(yAdapter);

        ArrayAdapter<String> xAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, xArray);
        xAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xSpinner.setAdapter(xAdapter);

        ArrayAdapter<String> textSizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, textSizeArray);
        textSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSizeSpinner.setAdapter(textSizeAdapter);
    }

    private String getTextSizeSpinnerValue() {
        return textSizeSpinner.getSelectedItem().toString();
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
