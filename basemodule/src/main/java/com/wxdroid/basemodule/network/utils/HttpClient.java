package com.wxdroid.basemodule.network.utils;


import com.wxdroid.basemodule.network.HttpRequest;
import com.wxdroid.basemodule.network.HttpResponse;
import com.wxdroid.basemodule.network.HttpTask;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClient {

    public static final int TIME_OUT = 15 * 1000;
    private static String sUserAgent;
    private static HttpClient sInstance;
    private OkHttpClient mClient;

    public HttpClient() {
        mClient = newClient();
    }

    public static HttpClient getInstance() {
        if (sInstance == null) {
            synchronized (HttpClient.class) {
                if (sInstance == null) {
                    sInstance = new HttpClient();
                }
            }
        }
        return sInstance;
    }

    public static HttpTask addRequest(HttpRequest request) {
        if (request == null){
            return null;
        }
        Request requestInfo = getRequest(request);
        Call call = getInstance().getClient().newCall(requestInfo);
        HttpTask task = new HttpTask();
        task.setRequest(request);
        task.enqueue(call);
        return task;
    }

    public static HttpResponse addRequestSync(HttpRequest request) {
        if (request == null){
            return null;
        }

        Call call = getInstance().getClient().newCall(getRequest(request));
        try {
            return new HttpResponse(call.execute());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OkHttpClient getClient(){
        return mClient;
    }

    /**
     * 创建默认的OkHttpClient
     */
    private static OkHttpClient newClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(false);
        //builder.sslSocketFactory(getSSLSocketFactory());
        return builder.build();
    }

    private static Request getRequest(HttpRequest request) {
        if (request == null)
            return null;

        Request.Builder builder = new Request.Builder();
        String url = request.getUrl();
        if (HttpRequest.HttpMethod.GET == request.mMethod)
        {
            url += request.getParameterForGet();
        }
        //LOG
//        LiveLog.d("http", "url = "+url);
        builder.url(url);
        builder.tag(request.getTag());
        builder.headers(request.getRequestHeaders());
        switch (request.mMethod) {
            case HttpRequest.HttpMethod.GET:
                builder.get();
                break;
            case HttpRequest.HttpMethod.POST:
                if (request.isFilePost() || request.isStreamPost())
                {
                    builder.post(request.getRequestBodyFile());
                }
                else
                {
                    builder.post(request.getRequestBody());
                }
                break;
            default:
                throw new RuntimeException("Unsupported http method.");
        }
        return builder.build();
    }

    /**
     * 获取SSLSocketFactory
     */
//    private static SSLSocketFactory getSSLSocketFactory() {
//        try {
//            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//            keyStore.load(null);
//            final int[] ids = new int[]{R.raw.huajiao1, R.raw.huajiao2, R.raw.huajiao3, R.raw.huajiao4};
//            for (int i = 0; i < ids.length; i++) {
//                keyStore.setCertificateEntry("cert" + i, loadCertificate(ids[i]));
//            }
//            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            factory.init(keyStore);
//            SSLContext ssl = SSLContext.getInstance("TLS");
//            ssl.init(null, factory.getTrustManagers(), null);
//            return ssl.getSocketFactory();
//        } catch (Exception e) {
//            return null;
//        }
//    }


    /**
     * 加载证书
     */
//    private static X509Certificate loadCertificate(int id) {
//        InputStream in = null;
//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            in = BaseApplication.getContext().getResources().openRawResource(id);
//            X509Certificate cert = (X509Certificate) cf.generateCertificate(in);
//            return cert;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                }
//            }
//        }
//    }


}
