package com.wxdroid.basemodule.network.request;




import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpRequest;

import java.io.IOException;

import okhttp3.Response;

public class StringRequest extends HttpRequest<String>
{
    StringRequesetListener m_string_request_listener;
    public StringRequest(String url, StringRequesetListener listener) {
        this(HttpMethod.GET, url, listener);
    }

    public StringRequest(int method, String url, StringRequesetListener listener) {
        super(method, url, null);
        m_string_request_listener = listener;
    }

    @Override
    public void onResponse(Response response) {
        if (m_string_request_listener == null)
            return;

        if (response == null || response.body() == null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_INVALID_VALUE));
            return;
        }

        String txtResp = parseString(response);
        if (txtResp == null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_PARSE));
            return;
        }
        onResponse(txtResp);
    }

    protected String parseString(Response response){
        if (response == null)
            return null;

        try {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onResponse(final String str) {
        m_string_request_listener.onAsyncResponse(str);
        postOnMain(new Runnable() {
            @Override
            public void run() {
                m_string_request_listener.onResponse(str);
            }
        });

    }
    @Override
    public void onFailure(final HttpError e) {
        if(m_string_request_listener==null) return;
        postOnMain(new Runnable() {
            @Override
            public void run() {
                m_string_request_listener.onFailure(e);
            }
        });
    }
}
