package com.github.bilbobx182.mirrorm1rr0r;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doneButton = (Button) findViewById(R.id.commitButton);
        if (!isCoarse()) {
            createRequestDialogue();
        }
        doneButton.setOnClickListener(v -> {
            Intent piConnection = new Intent(getBaseContext(), PiBluetooth.class);
            startActivity(piConnection);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Handle the case where we cant access location
            Log.d("Ciaran", "SHITT");
        }
    }

    private void createRequestDialogue() {
        AlertDialog.Builder permissionPopup = new AlertDialog.Builder(MainActivity.this);
        permissionPopup.setMessage("We need them perms");

        permissionPopup.setPositiveButton("Yes",
                (dialog, id) -> {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                });

        permissionPopup.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = permissionPopup.create();
        alert11.show();

    }

    // "Sand is Coarse, Not like your skin Padame"
    private boolean isCoarse() {
        return getApplicationContext().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, android.os.Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_GRANTED;
    }

//    private String validateInput() {
//        EditText username = (EditText) findViewById(R.id.nameEditText);
//        EditText rawPassword = (EditText) findViewById(R.id.passwordEditText);
//
//        //// TODO: 27/09/2017 Add a check to see if the username is already there. But I suppose this is the login Screen.
//        if (username.getText() != null && rawPassword.getText() != null) {
//            String toHash = username.getText() + "" + rawPassword.getText();
//            Encryptor encryptor = new Encryptor();
//            return encryptor.hashCode(toHash);
//
//        } else {
//            return "Woops empty fields";
//        }
//    }
}
