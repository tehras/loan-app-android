package com.github.koshkin.loanapplication.network;

/**
 * Created by tehras on 5/16/15.
 */
public class Response<T> {

    private T responseObject;
    private ResponseCode mResponseCode;

    public ResponseCode getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        mResponseCode = responseCode;
    }

    public void setResponseCode(int responseCode) {
        mResponseCode = ResponseCode.fromCode(responseCode);
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public enum ResponseCode {
        SUCCESS(200), FAILED(500);

        private int mCode;

        ResponseCode(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }

        public void setCode(int code) {
            mCode = code;
        }

        public static ResponseCode fromCode(int code) {
            for (ResponseCode i : values()) {
                if (i.getCode() == code) return i;
            }
            return FAILED;
        }
    }

}
