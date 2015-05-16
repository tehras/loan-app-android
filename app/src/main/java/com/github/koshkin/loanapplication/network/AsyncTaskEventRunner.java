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

    public void executeTask() {
        Request request = new Request();
        request.setReqId(mReqId);

        NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(mAsyncTaskCallbackInterceptor, mAsyncTaskParser, request);
        networkAsyncTask.execute("");
    }


}
