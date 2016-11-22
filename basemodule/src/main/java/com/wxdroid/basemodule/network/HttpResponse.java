package com.wxdroid.basemodule.network;


import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

public class HttpResponse {
    private final Response mResponse;

    public HttpResponse(Response response){
        mResponse = response;
    }

    public String getString(){
        if (mResponse == null)
            return null;

        try {
            return mResponse.body().string();
        } catch (IOException e) {
        }
        return null;
    }

    public InputStream getByteStream(){
        return (mResponse == null) ? null : mResponse.body().byteStream();
    }

    public long getContentLength(){
        return (mResponse == null) ? 0 : mResponse.body().contentLength();
    }

    public Response getResponse() {
        return mResponse;
    }
}
