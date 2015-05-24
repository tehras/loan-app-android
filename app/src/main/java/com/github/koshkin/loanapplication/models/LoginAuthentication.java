package com.github.koshkin.loanapplication.models;

import com.github.koshkin.loanapplication.network.AsyncTaskParser;
import com.github.koshkin.loanapplication.utils.NullChecker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tehras on 5/23/15.
 */
public class LoginAuthentication implements AsyncTaskParser {

    private static final String JSON_PARSER_ACCESS_TOKEN = "access_token";
    private static final String JSON_PARSER_TOKEN_TYPE = "token_type";
    private static final String JSON_PARSER_REFRESH_TOKEN = "refresh_token";

    private String mAccessToken;
    private String mTokenType;
    private String mRefreshToken;

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public String getTokenType() {
        return mTokenType;
    }

    public void setTokenType(String tokenType) {
        mTokenType = tokenType;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }

    @Override
    public Object parseResponse(String response) throws JSONException {
        if (NullChecker.isNullOrEmpty(response))
            return this;

        JSONObject reader = new JSONObject(response);

        if (reader.has(JSON_PARSER_ACCESS_TOKEN))
            setAccessToken(reader.getString(JSON_PARSER_ACCESS_TOKEN));
        if (reader.has(JSON_PARSER_TOKEN_TYPE))
            setTokenType(reader.getString(JSON_PARSER_TOKEN_TYPE));
        if (reader.has(JSON_PARSER_REFRESH_TOKEN))
            setRefreshToken(reader.getString(JSON_PARSER_REFRESH_TOKEN));

        return this;
    }
}
