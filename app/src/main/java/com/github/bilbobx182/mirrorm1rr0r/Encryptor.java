package com.github.bilbobx182.mirrorm1rr0r;

/**
 * Created by CiaranLaptop on 26/09/2017.
 */

import android.util.Log;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {

    public  Encryptor() {
    }

    public  Encryptor(String password) {
        String hashed = hashCode(password);
//        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
//        CreateQueueRequest create_request = new CreateQueueRequest()
//                .addAttributesEntry("DelaySeconds", "60")
//                .addAttributesEntry("MessageRetentionPeriod", "86400");
//
//        try {
//            sqs.createQueue(create_request);
//        } catch (AmazonSQSException e) {
//            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
//                throw e;
//            }
//        }
    }

    public String hashCode(String password) {
        StringBuffer hashedPassword = new StringBuffer();
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());

            byte[] messageDigestMD5 = messageDigest.digest();
            for (byte bytes : messageDigestMD5) {
                hashedPassword.append(String.format("%02x", bytes & 0xff));

            }
            Log.d("TAG","digestedMD5(hex):" + hashedPassword.toString());
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
        }

        return String.valueOf(hashedPassword);
    }
}
