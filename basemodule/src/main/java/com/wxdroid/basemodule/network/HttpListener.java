package com.wxdroid.basemodule.network;

public interface HttpListener<T> {
    void onResponse(T response);
    void onFailure(HttpError e);
    void onAsyncResponse(T response);
}
