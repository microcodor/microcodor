package com.wxdroid.basemodule.network.request;


import com.wxdroid.basemodule.network.HttpError;

/**
 * Created by like on 2016/11/1.
 */

public interface ModelRequestListener<T>{

    void onResponse(T bean);

    void onFailure(HttpError e, int errno, String msg, T bean);

    void onAsyncResponse(T bean);
}
