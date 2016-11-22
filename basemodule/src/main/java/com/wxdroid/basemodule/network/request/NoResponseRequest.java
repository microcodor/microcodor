package com.wxdroid.basemodule.network.request;




import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpRequest;

import okhttp3.Response;

public class NoResponseRequest extends HttpRequest<Object>
{

    public NoResponseRequest(String url) {
        this(HttpMethod.GET, url);
    }
    public NoResponseRequest(int method, String url) {
        super(method, url, null);
    }

    @Override
    public void onFailure(HttpError e) {
    }

    @Override
    public void onResponse(Response response) {
    }
}
