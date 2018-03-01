package com.github.bilbobx182.finalyearproject;

/**
 * Created by CiaranLaptop on 02/02/2018.
 */

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public  Encryptor() {
    }

    public String hashCode(String input) {
        StringBuffer hashedInput = new StringBuffer();
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());

            byte[] messageDigestMD5 = messageDigest.digest();
            for (byte bytes : messageDigestMD5) {
                hashedInput.append(String.format("%02x", bytes & 0xff));

            }
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return String.valueOf(hashedInput);
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
