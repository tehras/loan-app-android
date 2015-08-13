package com.github.koshkin.loanapplication.network;

/**
 * Created by tehras on 5/16/15.
 */
public class URLConstants {

    public static final String LOAN_SERVICE_BASE_URL = "http://1668b726.ngrok.com";
    public static final String POST_LOGIN_URL = "/oauth/token?password=%s&username=%s&grant_type=password&scope=read&write&client_secret=123456&client_id=clientapp";
    public static final String GET_LOAN_LIST_URL = "/loans";
    public static final String GET_HOME_GRAPH_URL = "http://0acbcd59.ngrok.io/loans";
}
