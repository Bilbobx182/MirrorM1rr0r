package com.github.bilbobx182.finalyearproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class SendMessage extends Fragment implements View.OnClickListener {
    private Spinner ySpinner;
    private Spinner xSpinner;
    private Spinner textSizeSpinner;
    private EditText queryInputEditText;
    private Button doneButton;

    private TextView mirrorLocationHelperTextView;
    private TextView colourPickerHelperTextView;
    private TextView textSizeHelperTextView;
    private TextView querySubTextView;
    private TextView queryHeaderTextView;

    private android.support.v7.widget.GridLayout colourGridLayout;
    private ImageButton blueButton;
    private ImageButton greenButton;
    private ImageButton whiteButton;
    private ImageButton yellowButton;
    private ImageButton redButton;

    private TextView colourSelector;
    private String activeColour = "White";

    private ProgressBar progressBar;
    private TextView progressText;

    private Switch advancedMode;
    private Spinner advancedModeSpinner;
    private boolean isAdvanced;

    private String lat;
    private String lon;

    private OnFragmentInteractionListener mListener;

    public SendMessage() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        colourSelector = getActivity().findViewById(R.id.colourPickerHelper);
        colourGridLayout = getActivity().findViewById(R.id.colourGrid);

        queryInputEditText = getView().findViewById(R.id.queryEditText);

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

        progressBar = getView().findViewById(R.id.progressBarSendMessage);
        progressText = getView().findViewById(R.id.progressBarTextSendMessage);
        displayProgressBar(false);

        advancedMode = getActivity().findViewById(R.id.advancedMessage);
        advancedMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAdvanced = isChecked;
            shouldDisplayAdvancedOptions(isChecked);
            setLatAndLong();
        });

        queryHeaderTextView = getView().findViewById(R.id.queryHeaderText);
        querySubTextView = getView().findViewById(R.id.querySubText);
        mirrorLocationHelperTextView = getView().findViewById(R.id.mirrorLocationHelper);
        textSizeHelperTextView = getView().findViewById(R.id.textSizeHelper);
        colourPickerHelperTextView = getView().findViewById(R.id.colourPickerHelper);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflated = inflater.inflate(R.layout.fragment_send_message, container, false);

        ySpinner = inflated.findViewById(R.id.ySpinner);
        xSpinner = inflated.findViewById(R.id.xSpinner);

        textSizeSpinner = inflated.findViewById(R.id.textSizeSpinner);
        advancedModeSpinner = inflated.findViewById(R.id.advancedOptionsSpinner);

        setupSpinners();
        return inflated;
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
        // This looks weird on fast devices. But it's great for slow devices
        displayProgressBar(true);
        hideSendMessageContents(true);

        beginMessageTransformation();
    }

    private void displayProgressBar(boolean shouldDisplay) {
        if (shouldDisplay) {
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
        }
    }

    private void hideSendMessageContents(boolean shouldHide) {
        if (shouldHide) {
            queryHeaderTextView.setVisibility(View.GONE);
            querySubTextView.setVisibility(View.GONE);
            colourPickerHelperTextView.setVisibility(View.GONE);
            mirrorLocationHelperTextView.setVisibility(View.GONE);
            textSizeHelperTextView.setVisibility(View.GONE);
            queryInputEditText.setVisibility(View.GONE);

            xSpinner.setVisibility(View.GONE);
            ySpinner.setVisibility(View.GONE);
            textSizeSpinner.setVisibility(View.GONE);

            colourGridLayout.setVisibility(View.GONE);
        }
    }

    private void beginMessageTransformation() {
        groupValuesBeforeSending();
    }


    private void groupValuesBeforeSending() {
        HashMap<String, String> messageValues = new HashMap<>();
        HashMap<String, String> colourAndHex = new HashMap<>();
        colourAndHex.put("Blue", "0099cc");
        colourAndHex.put("Green", "99cc00");
        colourAndHex.put("White", "ffffff");
        colourAndHex.put("Yellow", "fdd835");
        colourAndHex.put("Red", "ff4444");


        String[] spinnerValues = getSpinnerValue();
        String input = "";

        DBManager dbManager = new DBManager(getActivity());
        if (isAdvanced) {
            input = getAdvancedSpinnerValue();
            messageValues.put("lat", lat);
            messageValues.put("long", lon);
        } else {
            input = queryInputEditText.getText().toString();
        }
        messageValues.put("message", input);
        messageValues.put("location", String.valueOf(spinnerValues[0]) + "," + String.valueOf(spinnerValues[1]));
        messageValues.put("fontsize", getTextSizeSpinnerValue());
        messageValues.put("fontcolour", colourAndHex.get(activeColour));

        // No need to store the values if it's a dynamic widget
        if(!isAdvanced) {
            populateDatabaseWithMessage(messageValues);
        }

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
        String advancedOptions[] = {"Weather current location", "Temperature current location"};

        ArrayAdapter<String> yAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yArray);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ySpinner.setAdapter(yAdapter);

        ArrayAdapter<String> xAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, xArray);
        xAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        xSpinner.setAdapter(xAdapter);

        ArrayAdapter<String> textSizeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, textSizeArray);
        textSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textSizeSpinner.setAdapter(textSizeAdapter);

        ArrayAdapter<String> advancedOptionsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, advancedOptions);
        advancedOptionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        advancedModeSpinner.setAdapter(advancedOptionsAdapter);
    }

    private String getTextSizeSpinnerValue() {
        return textSizeSpinner.getSelectedItem().toString();
    }

    private String getAdvancedSpinnerValue() {
        if (textSizeSpinner.getSelectedItem().toString().contains("Weather")) {
            return "^/^weather";
        } else {
            return "^/^temperature";
        }
    }

    private void setLatAndLong() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPerms();
        }

        LocationManager locationManager;
        /*
        Desc: Modified version of code from SO
        Reference: https://stackoverflow.com/questions/20438627/getlastknownlocation-returns-null
        Last Accessed: 20/March/2018
         */
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location mostAccurateLocation = null;

        for (String provider : providers) {
            Location currentProviderLocation = locationManager.getLastKnownLocation(provider);
            if (mostAccurateLocation == null) {
                if (currentProviderLocation != null) {
                    mostAccurateLocation = currentProviderLocation;
                }
            } else if ((currentProviderLocation != null) && currentProviderLocation.getAccuracy() < mostAccurateLocation.getAccuracy()) {
                mostAccurateLocation = currentProviderLocation;
            }
        }
        // End Reference

        setLat(String.valueOf(mostAccurateLocation.getLatitude()));
        setLon(String.valueOf(mostAccurateLocation.getLongitude()));
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

    private void shouldDisplayAdvancedOptions(boolean shouldDisplay) {
        if (shouldDisplay) {
            queryInputEditText.setVisibility(View.GONE);
            advancedModeSpinner.setVisibility(View.VISIBLE);
        } else {
            queryInputEditText.setVisibility(View.VISIBLE);
            advancedModeSpinner.setVisibility(View.GONE);
        }
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    private void requestPerms() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }
}
