package com.github.koshkin.loanapplication.network;

import org.json.JSONException;

/**
 * Created by tehras on 5/16/15.
 */
public interface AsyncTaskParser<T> {

    public T parseResponse(String response) throws JSONException;
}
