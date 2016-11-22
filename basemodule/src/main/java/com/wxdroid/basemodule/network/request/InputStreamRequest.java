package com.wxdroid.basemodule.network.request;



import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpListener;
import com.wxdroid.basemodule.network.HttpRequest;

import java.io.InputStream;

import okhttp3.Response;

public class InputStreamRequest extends HttpRequest<InputStream>
{

    public InputStreamRequest(String url, HttpListener listener) {
        this(HttpRequest.HttpMethod.GET, url, listener);
    }

    public InputStreamRequest(int method, String url, HttpListener listener) {
        super(method, url, listener);
    }

    @Override
    public void onResponse(Response response) {
        if (mListener == null)
            return;

        if (response == null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_INVALID_VALUE));
            return;
        }

        InputStream stream = response.body().byteStream();
        mListener.onResponse(stream);
    }
}
