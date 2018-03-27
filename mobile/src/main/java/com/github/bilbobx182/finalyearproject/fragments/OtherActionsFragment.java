package com.github.bilbobx182.finalyearproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.bilbobx182.finalyearproject.DBManager;
import com.github.bilbobx182.finalyearproject.R;
import com.github.bilbobx182.sharedcode.RequestPerformer;

import java.sql.SQLException;
import java.util.HashMap;


public class OtherActionsFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private Button clearButton;

    public OtherActionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        clearButton = getActivity().findViewById(R.id.additionalFunctionalityClearMirrorButton);
        clearButton.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_actions, container, false);
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
        System.out.println(";o");
        switch (view.getId()) {
            case (R.id.additionalFunctionalityClearMirrorButton): {

                try {
                    DBManager dbManager = new DBManager(getContext());
                    dbManager.open();

                    HashMap<String, String> messageValues = new HashMap<>();
                    messageValues.put("message", "@@clear");
                    messageValues.put("queueurl", dbManager.getUserInformationByColumn("queue"));
                    dbManager.close();

                    RequestPerformer requestPerformer = new RequestPerformer();
                    requestPerformer.performSendMessage(messageValues);
                    getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
