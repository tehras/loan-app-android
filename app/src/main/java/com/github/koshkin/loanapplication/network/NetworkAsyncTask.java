package com.github.koshkin.loanapplication.network;

import android.os.AsyncTask;
import android.util.Log;

import com.github.koshkin.loanapplication.utils.NullChecker;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tehras on 5/16/15.
 */
public class NetworkAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "NetworkAsyncTask";

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
            else
                sendPost();
        } catch (IOException e) {
            mResponse.setResponseCode(500);
            mFragmentCallbackInterceptor.onCallbackReceived(mResponse, mRequest);
        }
        return null;
    }

    private void sendPost() throws IOException {
        String url = String.format(mRequest.getUrl(), mRequest.getParams());
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(mRequest.getRequestMethod().getMethod());
        connection.setRequestProperty("Authorization", "Y2xpZW50YXBwOjEyMzQ1Ng==");

        if (mRequest.getHeaders() != null) {
            for (String key : mRequest.getHeaders().keySet()) {
                connection.setRequestProperty(key, mRequest.getHeaders().get(key));
            }
        }

        if (!NullChecker.isNullOrEmpty(mRequest.getRequestObject())) {
            Log.v(getClass().getSimpleName(), "requestObject - " + mRequest.getRequestObject());

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(mRequest.getRequestObject());

            writer.flush();
            writer.close();
            os.close();
        }

        parseResponse(connection);
    }

    private void sendGet() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(mRequest.getUrl()).openConnection();
        connection.addRequestProperty("Authorization", "Bearer a3c67de7-1611-4593-b8c7-f276b6beb553");
        connection.setRequestMethod(mRequest.getRequestMethod().getMethod());

        parseResponse(connection);

        Log.v(TAG, "ResponseCode - " + mResponse.getResponseCode());
    }


    private void parseResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();

        Log.v(getClass().getSimpleName(), "ResponseCode - " + responseCode);

        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            try {
                mResponse.setResponseObject(mAsyncTaskParser.parseResponse(response.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mResponse.setResponseCode(responseCode);
    }

    @Override
    protected void onPostExecute(String s) {
        mFragmentCallbackInterceptor.onCallbackReceived(mResponse, mRequest);
    }
}
