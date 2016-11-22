package com.wxdroid.basemodule.network;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;


import com.wxdroid.basemodule.network.request.ProgressRequestBody;
import com.wxdroid.basemodule.network.utils.HttpClient;
import com.wxdroid.basemodule.network.utils.StringUtil;
import com.wxdroid.basemodule.network.utils.ThreadHelper;
import com.wxdroid.basemodule.network.utils.UploadStreamDescriber;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class HttpRequest<T> {
    public static final String TAG = HttpRequest.class.getSimpleName();
    protected boolean cancel = false;// 是否终止
    protected boolean retrysuccess = false;//是否请求成功，重试过程如果成功。则不进行回调

    public class HttpMethod {
        public static final int DEPRECATED_GET_OR_POST = -1;
        public static final int GET = 0;
        public static final int POST = 1;
        public static final int PUT = 2;
        public static final int DELETE = 3;
        public static final int HEAD = 4;
        public static final int OPTIONS = 5;
        public static final int TRACE = 6;
        public static final int PATCH = 7;
    }

    private String mUrl;
    public final int mMethod;
    public final HttpListener<T> mListener;
    protected Object mTag;
    protected HashMap<String, String> mPostParams;      //post的参数
    protected HashMap<String, String> mHeaders;
    protected HashMap<String, Object> mSecurityPostParams;      //post的加密参数

    protected HashMap<String ,String> m_get_params;
    protected LinkedList<String> m_files;
    protected HashMap<String, String> filesMap;
    protected Map<String, UploadStreamDescriber> streamMap;

    private int retrycount = 3;// 重试次数
    private int retrycurr = 0;
    private long retrysleep = 3000;// 重试间隔
    protected boolean retry = true;
    public void setRetry(boolean retry) {
        this.retry = retry;
    }

    private ProgressRequestBody.ProgressRequestListener progressRequestListener;

    public HttpRequest(String url, HttpListener listener) {
        this(-1, url, listener);
    }

    public HttpRequest(int method, String url, HttpListener listener) {
        this.mMethod = method;
        this.mUrl = url;
        this.mListener = listener;
    }

    /**
     * 设置上传进度回调
     * @param progressRequestListener
     */
    public void setProgressRequestBody(ProgressRequestBody.ProgressRequestListener progressRequestListener) {
        this.progressRequestListener = progressRequestListener;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    public Object getTag() {
        return mTag;
    }

    public void onFailure(final HttpError e) {
//        if(retryCall()) {
//            return;
//        }
        if(cancel) {
            return;
        }
        postOnMain(new Runnable() {
            @Override
            public void run() {
                if (mListener != null)
                    mListener.onFailure(e);
            }
        });
    }

    public void onResponse(Response response) {
    }

    /**
     * 网络失败。重试
     * @return
     */
//    private boolean retryCall() {
//        // 重试逻辑
//        while (retrycurr < retrycount && retry) {
//            try {
//                Thread.sleep(retrysleep);
//            } catch (InterruptedException e1) {
//                e1.printStackTrace();
//            }
//            if(cancel) {
//                retrycurr = 0;
//                return true;
//            }
//            // 替换新的guid
//            String uriStr = getUrl();
//            Uri uri = Uri.parse(uriStr);
//            String tempguid = uri.getQueryParameter("guid");
//
//            String time = uri.getQueryParameter("time");
//            String rand = uri.getQueryParameter("rand");
//            String network = uri.getQueryParameter("network");
//            if (!TextUtils.isEmpty(tempguid)) {
//                // 替换新的guid
//                ExtraInfo info = com.huajiao.network.HttpUtils.getExtraInfo();
//                String newguid = Security.init(BaseApplication.getContext(), info, AppEnv.APP_ENCRIPT);
//                uriStr = uriStr.replace("time=" + time, "time=" + info.time)
//                .replace("rand="+rand, "rand="+info.rand)
//                .replace("network="+network, "network="+info.network)
//                .replace("guid=" + tempguid, "guid=" + newguid);
//                setUrl(uriStr);
//            }
//            HttpResponse httpResponse = HttpClient.retryRequestSync(this);
//            if(cancel) {
//                retrycurr = 0;
//                return true;
//            }
//            if(httpResponse != null) {
//                retrysuccess = true;
//                retry = false;
//                onResponse(httpResponse.getResponse());
//                retrycurr = 0;
//                return true;
//            }
//            retrycurr++;
//        }
//        retry = false;
//        retrycurr = 0;
//        return false;
//    }

    @SuppressWarnings("unchecked")
    public RequestBody getRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        HashMap<String, String> mParams = getPostParameter();
        if (mParams != null && mParams.size() > 0) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                String key = entry.getKey();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    public boolean isFilePost()
    {
        boolean b_ret = false;
        if (m_files != null && m_files.size() > 0)
        {
            b_ret = true;
        }
        if(filesMap != null && filesMap.size() > 0) {
            b_ret = true;
        }
        return b_ret;
    }

    public boolean isStreamPost()
    {
        boolean b_ret = false;
        if (streamMap != null && streamMap.size() > 0)
        {
            b_ret = true;
        }
        return b_ret;
    }

    @SuppressWarnings("unchecked")
    public RequestBody getRequestBodyFile() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        HashMap<String, String> mParams = getPostParameter();
        if (mParams != null && mParams.size() > 0) {
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                String key = entry.getKey();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, value));
            }
        }

        if (m_files != null && m_files.size() > 0)
        {
            for (int i = 0; i < m_files.size(); ++i)
            {
                ProgressRequestBody fileBody = new ProgressRequestBody(MediaType.parse(guessMimeType(m_files.get(i))),new File(m_files.get(i)), progressRequestListener);
//                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(m_files.get(i))), new File(m_files.get(i)));
                builder.addFormDataPart("file", m_files.get(i), fileBody);
            }
        }
        if(filesMap != null && filesMap.size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = filesMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                File file = new File(next.getValue());
                ProgressRequestBody fileBody = new ProgressRequestBody(MediaType.parse(getMimeType(file.getName())),file, progressRequestListener);
//                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(m_files.get(i))), new File(m_files.get(i)));
                builder.addFormDataPart(next.getKey(), next.getValue(), fileBody);
            }
        }

        if(streamMap != null && streamMap.size() > 0) {
            Iterator<Map.Entry<String, UploadStreamDescriber>> iterator = streamMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, UploadStreamDescriber> next = iterator.next();
                UploadStreamDescriber describer = next.getValue();
                if(describer != null){
                    String extention = MimeTypeMap.getSingleton().getExtensionFromMimeType(describer.mimeType);
                    if("jpeg".equals(extention)){
                        extention = "jpg";
                    } else if (TextUtils.isEmpty(extention) && describer.mimeType != null) {
                        if (describer.mimeType.contains("jpg")) {
                            extention = "jpg";
                        } else if (describer.mimeType.contains("png")) {
                            extention = "png";
                        }
                    }
                    ProgressRequestBody inputstreamBody = new ProgressRequestBody(MediaType.parse(describer.mimeType),describer.inputStream, describer.length, progressRequestListener);
                    builder.addFormDataPart(next.getKey(), next.getKey()+"."+extention, inputstreamBody);
//                        builder.addFormDataPart(next.getKey(), describer.inputStream,describer.length,next.getKey()+"."+extention,describer.mimeType);
                }
            }
        }


        return builder.build();
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try
        {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

//    private String guessMimeTypeBy(String mimeType) {
//        String extention = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
//        if("jpeg".equals(extention)){
//            extention = "image/jpg";
//        } else if (TextUtils.isEmpty(extention) && mimeType != null) {
//            if (mimeType.contains("jpg")) {
//                extention = "image/jpg";
//            } else if (mimeType.contains("png")) {
//                extention = "image/png";
//            }
//        }
//        return extention;
//    }
    private String getMimeType(String filename) {
        String result;
        if(filename.endsWith("jpeg") || filename.endsWith("jpg")){
            result = "image/jpeg";
        }else if(filename.endsWith("gif")){
            result = "image/gif";
        }else if(filename.endsWith("png")){
            result = "image/png";
        }else if(filename.endsWith("bmp")){
            result = "image/bmp";
        }else {
            result = "application/octet-stream";
        }
        return result;
    }


    @SuppressWarnings("unchecked")
    public Headers getRequestHeaders() {
        Headers.Builder builder = new Headers.Builder();
        HashMap<String, String> mHeaders = getHeaders();
        if (mHeaders != null && mHeaders.size() > 0) {
            Iterator<Map.Entry<String, String>> it = mHeaders.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String key = entry.getKey();
                if (TextUtils.isEmpty(key)) {
                    continue;
                }
                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    public HttpRequest setPostParameters(HashMap<String, String> params) {
        mPostParams = params;

        return this;
    }

    public HttpRequest addPostParameter(String key, String value) {
        if (mPostParams == null) {
            mPostParams = new HashMap<>();
        }

        mPostParams.put(key, value);

        return this;
    }

    public HttpRequest addFiles(String str_file_path)
    {
        if (m_files == null)
        {
            m_files = new LinkedList<>();
        }
        m_files.add(str_file_path);

        return this;
    }

    public HttpRequest setFiles(HashMap<String, String> filesMap)
    {
        if (filesMap != null)
        {
            this.filesMap = filesMap;
        }

        return this;
    }

    public HttpRequest addStreams(String key, UploadStreamDescriber inputStream)
    {
        if (streamMap == null)
        {
            streamMap = new HashMap<String, UploadStreamDescriber>();
        }
        if(inputStream != null && !TextUtils.isEmpty(key)) {
            streamMap.put(key, inputStream);
        }

        return this;
    }

    public HttpRequest setStreams(HashMap<String, UploadStreamDescriber> streamMap)
    {
        if (streamMap != null)
        {
            this.streamMap = streamMap;
        }

        return this;
    }

    public HttpRequest setGetParameter(HashMap<String, String> params) {
        if(params != null) {
            this.m_get_params = params;
        }

        return this;
    }

    public HttpRequest addGetParameter(String key, String value)
    {
        if (m_get_params == null)
        {
            m_get_params = new HashMap<>();
        }
        if (value != null) {
            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        m_get_params.put(key, value);

        return this;
    }

    public String getParameterForGet()
    {
        if (m_get_params == null || m_get_params.size() == 0)
        {
            return "";
        }

        return StringUtil.organizeParams(m_get_params);
    }



    public HashMap<String, String> getPostParameter() {
        if (mPostParams == null){
            mPostParams = new HashMap<>();
        }
        return mPostParams;
    }

    public HttpRequest setHeaders(HashMap<String, String> params) {
        mHeaders = params;

        return this;
    }

    public HttpRequest addHeader(String key, String value) {
        if (mHeaders == null) {
            mHeaders = new HashMap<>();
        }

        mHeaders.put(key, value);

        return this;
    }

    public HashMap<String, String> getHeaders() {
        return mHeaders;
    }

    public final void postOnMain(Runnable runnable) {
        if (runnable == null)
            return;

        ThreadHelper.runOnUiThread(runnable);
    }

    public void addSecurityPostParameter(String key, Object value) {
        if (mSecurityPostParams == null) {
            mSecurityPostParams = new HashMap<>();
        }

        mSecurityPostParams.put(key, value);
    }

    public void cancel() {
        retry = false;
        cancel = true;
    }

    public HttpRequest execute(){
        HttpClient.addRequest(this);
        return null;
    }
}
