package com.wxdroid.basemodule.network.request;





import com.wxdroid.basemodule.network.HttpError;

import org.json.JSONObject;

public interface JsonRequestListener{
        void onResponse(JSONObject var1);
        void onFailure(HttpError e, int errno, String msg, JSONObject jsonObject);
    }