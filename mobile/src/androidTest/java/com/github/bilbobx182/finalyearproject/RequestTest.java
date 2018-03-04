package com.github.bilbobx182.finalyearproject;

import android.support.test.runner.AndroidJUnit4;

import com.github.bilbobx182.sharedcode.RequestPerformer;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

/**
 * Created by CiaranLaptop on 04/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RequestTest {

    RequestPerformer requestPerformer;
    @Test
    public void testSendMessageOutput() throws Exception {
        requestPerformer = new RequestPerformer();

        HashMap<String,String> queryValues = new HashMap<>();
        queryValues.put("queueurl","https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo");
        queryValues.put("message","test");

        RequestPerformer spyReqeustPerformer = spy(requestPerformer);

        String result = spyReqeustPerformer.performSendMessage(queryValues);
        assertEquals("https://tj5ur8uafi.execute-api.us-west-2.amazonaws.com/Prod/sendfifomessage?message=test&queueurl=https://sqs.eu-west-1.amazonaws.com/186314837751/ciaranVis.fifo", result);
    }
}



