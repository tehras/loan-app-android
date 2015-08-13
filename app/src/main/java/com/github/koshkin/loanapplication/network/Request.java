package com.github.koshkin.loanapplication.network;

import java.util.ArrayList;
import java.util.HashMap;

import static com.github.koshkin.loanapplication.network.URLConstants.GET_HOME_GRAPH_URL;
import static com.github.koshkin.loanapplication.network.URLConstants.GET_LOAN_LIST_URL;
import static com.github.koshkin.loanapplication.network.URLConstants.LOAN_SERVICE_BASE_URL;
import static com.github.koshkin.loanapplication.network.URLConstants.POST_LOGIN_URL;

/**
 * Created by tehras on 5/16/15.
 */
public class Request {
    public String getUrl() {
        return mReqId.getUrl();
    }

    public RequestMethod getRequestMethod() {
        return mReqId.getRequestMethod();
    }

    public enum ReqId {
        POST_LOGIN(LOAN_SERVICE_BASE_URL + POST_LOGIN_URL, RequestMethod.POST), GET_LOAN_LIST(LOAN_SERVICE_BASE_URL + GET_LOAN_LIST_URL, RequestMethod.GET), POST_NEW_LOAN(LOAN_SERVICE_BASE_URL + GET_LOAN_LIST_URL, RequestMethod.POST), GET_HOME_GRAPH(GET_HOME_GRAPH_URL, RequestMethod.GET);

        private String mUrl;
        private RequestMethod mRequestMethod;

        ReqId(String url, RequestMethod requestMethod) {
            mUrl = url;
            mRequestMethod = requestMethod;
        }

        public String getUrl() {
            return mUrl;
        }

        public void setUrl(String url) {
            mUrl = url;
        }

        public RequestMethod getRequestMethod() {
            return mRequestMethod;
        }

        public void setRequestMethod(RequestMethod requestMethod) {
            mRequestMethod = requestMethod;
        }
    }

    enum RequestMethod {
        GET("GET"), POST("POST");

        private String mMethod;

        public String getMethod() {
            return mMethod;
        }

        public void setMethod(String method) {
            mMethod = method;
        }

        RequestMethod(String method) {
            mMethod = method;
        }
    }

    private ReqId mReqId;
    private String mRequestObject;
    private HashMap<String, String> mHeaders;
    private ArrayList<String> mParams;

    public String getRequestObject() {
        return mRequestObject;
    }

    public void addHeader(String key, String value) {
        if (mHeaders == null)
            mHeaders = new HashMap<>();

        mHeaders.put(key, value);
    }

    public ArrayList<String> getParams() {
        if (mParams == null)
            return new ArrayList<>();
        return mParams;
    }

    public void addParam(String param) {
        if (mParams == null)
            mParams = new ArrayList<>();

        mParams.add(param);
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public void setRequestObject(String requestObject) {
        mRequestObject = requestObject;
    }

    public ReqId getReqId() {
        return mReqId;
    }

    public void setReqId(ReqId reqId) {
        mReqId = reqId;
    }
}
