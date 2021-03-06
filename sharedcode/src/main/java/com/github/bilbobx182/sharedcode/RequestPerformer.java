package com.github.bilbobx182.sharedcode;


import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * MIGRATED by CiaranLaptop on 18/02/2018.
 */

public class RequestPerformer {
    private static final String baseURL = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/";
    private static final String sendMessage = "sendfifomessage?";
    private static final String createQueue = "createfifoqueue?";
    private static HTTPAsyncRequest httpAsyncRequest;

    public RequestPerformer() {
        HTTPAsyncRequest httpAsyncRequest = new HTTPAsyncRequest();
        this.httpAsyncRequest = httpAsyncRequest;
    }

    public String performSendMessage(HashMap queryValues) {
        String messageValues = queryValues.toString().replaceAll(", ", "&");
        messageValues = messageValues.replaceAll("[{}]", "");

        String fullQuery = baseURL + sendMessage + messageValues;
        performOperation(fullQuery);
        return fullQuery;
    }

    public String createQueue(String hashedContents) {
        return (performOperation(baseURL + createQueue + "queueName=" + hashedContents));
    }

    public String performOperation(String fullQuery) {
        String result = "";
        httpAsyncRequest.execute(fullQuery);
        try {
            result = httpAsyncRequest.get().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

}
