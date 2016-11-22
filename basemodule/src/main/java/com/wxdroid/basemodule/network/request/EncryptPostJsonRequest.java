package com.wxdroid.basemodule.network.request;

import com.wxdroid.basemodule.network.HttpRequest;
import com.wxdroid.basemodule.network.utils.GsonUtils;
import com.wxdroid.basemodule.network.utils.HttpEncryptUtils;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class EncryptPostJsonRequest<T> extends JsonRequest {
    private static final String TAG = EncryptPostJsonRequest.class.getSimpleName();
    public EncryptPostJsonRequest(String url, JsonRequestListener listener) {
        this(HttpRequest.HttpMethod.POST,url, listener);
    }

    public EncryptPostJsonRequest(int method, String url, JsonRequestListener listener) {
        super(method, url, listener);
    }

    @Override
    public RequestBody getRequestBody() {
        String json;
        if (mSecurityPostParams != null && mSecurityPostParams.size() > 0) {
            json = GsonUtils.toJson(mSecurityPostParams);
        } else {
            json = "";
        }
        String content = HttpEncryptUtils.encryptAES(json, HttpEncryptUtils.key);
        RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), content);
        return body;
    }
}
