package com.github.koshkin.loanapplication.network;

/**
 * Created by tehras on 5/16/15.
 */
public class AsyncTaskEventRunner {

    private AsyncTaskCallbackInterceptor mAsyncTaskCallbackInterceptor;
    private AsyncTaskParser mAsyncTaskParser;
    private Request.ReqId mReqId;

    public AsyncTaskEventRunner(AsyncTaskCallbackInterceptor asyncTaskCallbackInterceptor, AsyncTaskParser asyncTaskParser, Request.ReqId reqId) {
        mAsyncTaskCallbackInterceptor = asyncTaskCallbackInterceptor;
        mAsyncTaskParser = asyncTaskParser;
        mReqId = reqId;
    }

    public void executeTask(String... params) {
        Request request = new Request();
        request.setReqId(mReqId);
        for (String param : params) request.addParam(param);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(mAsyncTaskCallbackInterceptor, mAsyncTaskParser, request);
        networkAsyncTask.execute(params);
    }

    public void executeTask(String requestObject) {
        Request request = new Request();
        request.setReqId(mReqId);
        request.setRequestObject(requestObject);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(mAsyncTaskCallbackInterceptor, mAsyncTaskParser, request);
        networkAsyncTask.execute();
    }

    public void executeTask(Request request) {
        request.setReqId(mReqId);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(mAsyncTaskCallbackInterceptor, mAsyncTaskParser, request);
        networkAsyncTask.execute();
    }

    public void executeTask() {
        Request request = new Request();
        request.setReqId(mReqId);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(mAsyncTaskCallbackInterceptor, mAsyncTaskParser, request);
        networkAsyncTask.execute();
    }


}
