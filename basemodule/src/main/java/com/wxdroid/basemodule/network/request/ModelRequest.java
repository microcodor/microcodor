package com.wxdroid.basemodule.network.request;

import android.text.TextUtils;

import com.wxdroid.basemodule.network.ClassUtils;
import com.wxdroid.basemodule.network.HttpBaseBean;
import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpRequest;
import com.wxdroid.basemodule.network.utils.GsonUtils;
import com.wxdroid.basemodule.utils.LogManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by like on 2016/11/1.
 */

public class ModelRequest<T extends HttpBaseBean> extends HttpRequest {

    private ModelRequestListener m_hf_modelrequest_listener;

    public ModelRequest(String url, ModelRequestListener listener) {
        this(HttpRequest.HttpMethod.GET, url, listener);
    }

    public ModelRequest(int method, String url, ModelRequestListener listener) {
        super(method, url, null);
        m_hf_modelrequest_listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(Response response) {
        super.onResponse(response);
        if (response == null) {
            HttpError error = new HttpError(HttpError.ERROR_MSG_READ_DATA, HttpError.ErrorType.ERROR_READ_DATA);
            InnerFailure(error, HttpError.ErrorType.ERROR_READ_DATA, HttpError.ERROR_MSG_READ_DATA, null);
            LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_READ_DATA));
            return;
        }
        if (m_hf_modelrequest_listener == null) {
            return;
        }
        Class<T> tClass = (Class<T>) ClassUtils.getTClass(m_hf_modelrequest_listener.getClass(), ModelRequestListener.class);
        if (tClass == null) {
            HttpError error = new HttpError(HttpError.ERROR_MSG_PARSER_GET_CLASS, HttpError.ErrorType.ERROR_PARSE);
            InnerFailure(error, HttpError.ErrorType.ERROR_PARSE, HttpError.ERROR_MSG_PARSER_GET_CLASS, null);
            LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_PARSER_GET_CLASS));
            return;
        }
        String data = getData(response);
        if (TextUtils.isEmpty(data)) {
            HttpError error = new HttpError(HttpError.ERROR_MSG_PARSER_DATA_NULL, HttpError.ErrorType.ERROR_DATA_NULL);
            InnerFailure(error, HttpError.ErrorType.ERROR_DATA_NULL, HttpError.ERROR_MSG_PARSER_DATA_NULL, null);
            LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_PARSER_DATA_NULL));
            return;
        }

        if (HttpBaseBean.class.isAssignableFrom(tClass)) {
            T t = (T) parseBaseBean(data, (Class<? extends HttpBaseBean>) tClass);
            final HttpBaseBean bean = t;
            if (bean == null) {
                HttpError error = new HttpError(HttpError.ERROR_MSG_PARSER_BEAN_NULL, HttpError.ErrorType.ERROR_PARSE);
                InnerFailure(error, HttpError.ErrorType.ERROR_PARSE, HttpError.ERROR_MSG_PARSER_BEAN_NULL, null);
                LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_PARSER_BEAN_NULL));
            } else {
                InnerRespones(bean);
            }
        } else {
            HttpError error = new HttpError(HttpError.ERROR_MSG_PARSER_CLASS_TYPE, HttpError.ErrorType.ERROR_PARSE);
            InnerFailure(error, HttpError.ErrorType.ERROR_PARSE, HttpError.ERROR_MSG_PARSER_CLASS_TYPE, null);
            LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_PARSER_CLASS_TYPE));
        }
    }

    @SuppressWarnings("unchecked")
    protected String getData(Response response) {
        String data = null;
        try {
            data = response.body().string();
        } catch (final IOException e) {
            e.printStackTrace();
            InnerFailure(new HttpError(e), HttpError.ErrorType.ERROR_PARSE, HttpError.ERROR_MSG_PARSER, null);
            LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -1, HttpError.ERROR_MSG_PARSER));
            data = "";
        }
        return data;
    }

    // 解析字符串
    public static <T extends HttpBaseBean> T parseBaseBean(String response, Class<T> tClass) {
        try {
            JSONObject data = new JSONObject(response);
            T bean = null;
            if (data != null) {
                bean = GsonUtils.fromJson(data.toString(), tClass);
            }
            if (bean == null) {
                bean = tClass.newInstance();
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onFailure(final HttpError e) {
        super.onFailure(e);
        if (retrysuccess) {
            return;
        }
        if (m_hf_modelrequest_listener == null) {
            return;
        }

        InnerFailure(e, HttpError.ErrorType.ERROR_NET_SERVER, HttpError.ERROR_MSG_BUSINESS, null);
        LogManager.getInstance().collectHttpLog(LogManager.getInstance().appendRequestResult(getUrl(), "", -99, HttpError.ERROR_MSG_BUSINESS));
    }

    @SuppressWarnings("unchecked")
    private void InnerFailure(final HttpError e, final int errno, final String msg, final HttpBaseBean bean) {
        if (m_hf_modelrequest_listener == null) {
            return;
        }
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if (m_hf_modelrequest_listener != null) {
                    m_hf_modelrequest_listener.onFailure(e, errno, msg, bean);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void InnerRespones(final HttpBaseBean bean) {
        if (m_hf_modelrequest_listener == null) {
            return;
        }
        m_hf_modelrequest_listener.onAsyncResponse(bean);
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if (m_hf_modelrequest_listener != null) {
                    m_hf_modelrequest_listener.onResponse(bean);
                }
            }
        });
    }

}
