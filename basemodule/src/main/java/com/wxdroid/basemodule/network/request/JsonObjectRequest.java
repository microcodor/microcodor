package com.wxdroid.basemodule.network.request;




import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpListener;
import com.wxdroid.basemodule.network.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class JsonObjectRequest extends HttpRequest<JSONObject>
{

    public JsonObjectRequest(String url, HttpListener listener) {
        this(HttpMethod.GET, url, listener);
    }

    public JsonObjectRequest(int method, String url, HttpListener listener) {
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

        JSONObject jsonObj = null;
        Exception error = null;
        try {
            jsonObj = new JSONObject(response.body().string());
        } catch (JSONException | IOException e) {
            error = e;
        }


        if (error != null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_PARSE));
            return;
        }

        onResponse(jsonObj);
    }

    protected void onResponse(final JSONObject jsonObject) {
        postOnMain(new Runnable() {
            @Override
            public void run() {
                mListener.onResponse(jsonObject);
            }
        });

    }
    @Override
    public void onFailure(final HttpError e) {
        if(mListener==null) return;
        postOnMain(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(e);
            }
        });
    }

}
