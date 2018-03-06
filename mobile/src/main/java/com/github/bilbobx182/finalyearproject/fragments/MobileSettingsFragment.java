package com.github.bilbobx182.finalyearproject.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;

import java.sql.SQLException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class MobileSettingsFragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button setProfileButton;
    private Button submitDetailsButton;
    private ImageView profileImageView;
    private EditText firstNameEditText;
    private EditText surnameEditText;
    private String picturePath = " ";
    private DBManager db;


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    public MobileSettingsFragment() {
        // Required empty public constructor
    }


    public static MobileSettingsFragment newInstance(String param1, String param2) {
        MobileSettingsFragment fragment = new MobileSettingsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mobile_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = new DBManager(getContext());
        bindWidgets();

//        if (!db.isFirstTime()) {
        if (!true) {
            firstNameEditText.setVisibility(View.GONE);
            surnameEditText.setVisibility(View.GONE);
        }

    }

    private void bindWidgets() {
        setProfileButton = getActivity().findViewById(R.id.setProfileButton);
        setProfileButton.setOnClickListener(this);

        submitDetailsButton = getActivity().findViewById(R.id.submitDetailsButton);
        submitDetailsButton.setOnClickListener(this);

        profileImageView = getActivity().findViewById(R.id.profileImageView);

        firstNameEditText = getActivity().findViewById(R.id.firstnameEditText);
        surnameEditText = getActivity().findViewById(R.id.surnameEditText);
    }

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
        if (view.getId() == R.id.setProfileButton) {

            checkPermissions();
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);

        } else if (view.getId() == R.id.submitDetailsButton) {

            EditText firstnameEditText = getView().findViewById(R.id.firstnameEditText);
            EditText surnameEditText = getView().findViewById(R.id.surnameEditText);

            updateSideBarWithUserInformation(firstnameEditText, surnameEditText);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {

            ImageView imageView = getView().findViewById(R.id.profileImageView);
            BitmapFactory.Options options = new BitmapFactory.Options();
            Uri chosenImage = data.getData();
            String[] pathToFile = {MediaStore.Images.Media.DATA};
            // Adjust sample size so it scales on the screen.
            options.inSampleSize = 2;

            Cursor cursor = getActivity().getContentResolver().query(chosenImage, pathToFile, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(pathToFile[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath, options));
        }
    }

    private void checkPermissions() {
        // We need to check permissions before the image is displayed as of Android 6.0...
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void updateSideBarWithUserInformation(EditText firstnameEditText, EditText surnameEditText) {
        try {
            db.open();

            if (picturePath != " ") {
                db.updateUserInformation("profilePath", picturePath);
            }

            if (db.isFirstBoot()) {
                db.updateUserInformation("firstname", firstnameEditText.getText().toString());
                db.updateUserInformation("surname", surnameEditText.getText().toString());

                getActivity().getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("initialBoot", false).apply();

                SetupMirrorFragment nextFrag = new SetupMirrorFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, nextFrag, "findThisFragment").addToBackStack(null).commit();

            } else {
                getActivity().finish();
                startActivity(getActivity().getIntent());
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
