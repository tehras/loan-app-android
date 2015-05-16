package com.github.koshkin.loanapplication.network;

/**
 * Created by tehras on 5/16/15.
 */
public interface AsyncTaskCallbackInterceptor {

    public void onCallbackReceived(Response response, Request request);
}
