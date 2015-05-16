package com.github.koshkin.loanapplication.network;

import static com.github.koshkin.loanapplication.network.URLConstants.GET_HOME_GRAPH_URL;
import static com.github.koshkin.loanapplication.network.URLConstants.GET_LOAN_LIST_URL;

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
        GET_LOAN_LIST(GET_LOAN_LIST_URL, RequestMethod.GET), GET_HOME_GRAPH(GET_HOME_GRAPH_URL, RequestMethod.GET);

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

    public ReqId getReqId() {
        return mReqId;
    }

    public void setReqId(ReqId reqId) {
        mReqId = reqId;
    }
}
