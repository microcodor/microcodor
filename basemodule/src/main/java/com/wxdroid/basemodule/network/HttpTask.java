package com.wxdroid.basemodule.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HttpTask implements Callback, IHttpTask {

    protected Call mCall;
    protected HttpRequest mRequest;

    protected boolean execute;

    public boolean enqueue(Call call){
        if (call == null || mCall != null)
            return false;

        mCall = call;
        mCall.enqueue(this);
        return true;
    }

    /**
     * 同步方式
     * @param call
     * @return
     */
    public boolean execute(Call call) {
        if (call == null/* || mCall != null*/) {
            return false;
        }

        mCall = call;
        try {
            Response response = mCall.execute();
            if (response != null) {
                onResponse(mCall, response);
            }
            return true;
        } catch (IOException e) {
            onFailure(mCall, e);// add onFailure 回调
        }
        return false;
    }

    public void setRequest(HttpRequest request){
        mRequest = request;
    }

    public HttpRequest getRequest(){
        return mRequest;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (mRequest != null)
            mRequest.onFailure(new HttpError(e));
    }

    @Override
    public void onResponse(Call call, Response response) {
        if (mRequest != null)
            mRequest.onResponse(response);
    }

    @Override
    public void cancel() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
        if(mRequest != null) {
            mRequest.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        return mCall != null && mCall.isCanceled();
    }
}
