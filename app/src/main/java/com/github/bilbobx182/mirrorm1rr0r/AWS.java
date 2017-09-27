package com.github.bilbobx182.mirrorm1rr0r;


import android.util.Log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

public class AWS {
    public void AWS() {

    }

    public void createQueue(String queueName) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAIDMELKX2YW6VOJ7Q", "H0yHtJv9zriNKRPCdR7WVE6snzicZ9qHiDLjl68A");
        AmazonSQSClient sqs = new AmazonSQSClient(credentials);

        CreateQueueRequest create_request = new CreateQueueRequest(queueName);
        try {
            sqs.createQueue(create_request);
        } catch (Exception e) {
            throw e;
        }
       Log.d("AWS.JAVA","QUEUE CREATED");

    }
}
