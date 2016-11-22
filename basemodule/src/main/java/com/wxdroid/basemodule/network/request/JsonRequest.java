package com.wxdroid.basemodule.network.request;




import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class JsonRequest extends HttpRequest<JSONObject>
{
    private static final String TAG=JsonRequest.class.getSimpleName();

    public final JsonRequestListener mJsonListener;




    public JsonRequest(String url, JsonRequestListener listener) {
        this(HttpMethod.GET, url, listener);
    }

    public JsonRequest(int method, String url, JsonRequestListener listener) {
        super(method, url, null);
        mJsonListener = listener;
    }

    @Override
    public void onFailure(HttpError e) {
        super.onFailure(e);
        if (mJsonListener == null)
            return;
        onFailure(e, -1, null, null);
    }

    protected JSONObject parseJsonObj(Response response){
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(response.body().string());
        } catch (JSONException | IOException e) {
        }
        return jsonObj;
    }

    protected void onResponse(final JSONObject jsonObject) {
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if(mJsonListener==null) return;
                mJsonListener.onResponse(jsonObject);
            }
        });

    }

    protected void onFailure(final HttpError e, final int errno, final String msg, final JSONObject jsonObject) {
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if(mJsonListener==null) return;
                mJsonListener.onFailure(e, errno, msg, jsonObject);
            }
        });
    }


    @Override
    public void onResponse(Response response) {
        if (mJsonListener == null)
            return;

        if (response == null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_INVALID_VALUE));
            return;
        }

        JSONObject jsonObj = parseJsonObj(response);

        if (jsonObj == null){
            onFailure(new HttpError("", HttpError.ErrorType.ERROR_PARSE));
            return;
        }
        int nErroNo = getJsonErrorNO(jsonObj);
        if (nErroNo != 0){
            onFailure(null, nErroNo, getJsonErrorMessage(jsonObj), jsonObj);
        }else{
            onResponse(jsonObj);
        }
    }

    private int getJsonErrorNO(JSONObject jsonObj){
        try {
            return jsonObj.getInt("errno");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private String getJsonErrorMessage(JSONObject jsonObj){
        try {
            return jsonObj.getString("errmsg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}