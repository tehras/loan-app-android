package com.github.koshkin.loanapplication.network;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tehras on 5/16/15.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, String> {

    private Response mResponse;
    private AsyncTaskCallbackInterceptor mFragmentCallbackInterceptor;
    private AsyncTaskParser mAsyncTaskParser;
    private Request mRequest;

    /**
     * TheAsyncTask is a generic asyncTask that will
     * create a request and return a response
     *
     * @param fragment
     * @param parser
     */
    NetworkAsyncTask(AsyncTaskCallbackInterceptor fragment, AsyncTaskParser parser, Request request) {
        mFragmentCallbackInterceptor = fragment;
        mAsyncTaskParser = parser;
        mRequest = request;
        mResponse = new Response();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (mRequest.getRequestMethod() == Request.RequestMethod.GET)
                sendGet();
        } catch (IOException e) {
            mResponse.setResponseCode(500);
            mFragmentCallbackInterceptor.onCallbackReceived(mResponse, mRequest);
        }
        return null;
    }

    private void sendGet() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(mRequest.getUrl()).openConnection();

        //Set Method
        connection.setRequestMethod(mRequest.getRequestMethod().getMethod());

        int responseCode = connection.getResponseCode();
        //TODO parse response code

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        //TODO parseResponse
        try {
            mResponse.setResponseObject(mAsyncTaskParser.parseResponse(response.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mResponse.setResponseCode(responseCode);
    }

    @Override
    protected void onPostExecute(String s) {
        mFragmentCallbackInterceptor.onCallbackReceived(mResponse, mRequest);
    }
}
