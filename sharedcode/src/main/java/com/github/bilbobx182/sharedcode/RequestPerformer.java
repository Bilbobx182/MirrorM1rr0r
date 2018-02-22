package com.github.bilbobx182.sharedcode;


import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * MIGRATED by CiaranLaptop on 18/02/2018.
 */

public class RequestPerformer {
    private static final String baseURL = "https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/";
    private static final String sendMessage = "sendfifomessage?";
    private static HTTPAsyncRequest httpAsyncRequest;

    public RequestPerformer() {
        HTTPAsyncRequest httpAsyncRequest = new HTTPAsyncRequest();
        this.httpAsyncRequest = httpAsyncRequest;
    }

    public String performSendMessage(HashMap queryValues) {
        String messageValues = queryValues.toString().replaceAll(", ", "&");
        messageValues = messageValues.replaceAll("[{}]", "");

        String fullQuery = baseURL + sendMessage+messageValues;
        performOperation(fullQuery);
        return  fullQuery;
    }

    public String performOperation(String fullQuery) {
        String result = "";
        //TODo Remove when I want to send requests again. Don't want to spam the API with requests.
//        httpAsyncRequest.execute(fullQuery);
//        try {
//            result = httpAsyncRequest.get().toString();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        return  result;
    }

}
