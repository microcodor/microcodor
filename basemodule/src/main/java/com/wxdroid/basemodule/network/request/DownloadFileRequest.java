package com.wxdroid.basemodule.network.request;


import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpListener;
import com.wxdroid.basemodule.network.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Response;

public abstract class DownloadFileRequest extends HttpRequest<File> implements ProgressResponseBody.ProgressResponseListener{

    public DownloadFileRequest(String url, HttpListener listener) {
        this(HttpMethod.GET, url, listener);
    }

    public DownloadFileRequest(int method, String url, HttpListener listener) {
        super(method, url, listener);
    }

    public abstract File getFile();
    @Override
    public void onResponse(Response response) {
        if (mListener == null)
            return;

        if (response == null||!response.isSuccessful()){
            onFailure(new HttpError("返回为空", HttpError.ErrorType.ERROR_INVALID_VALUE));
            return;
        }

        int progress = 0;

        InputStream in = null;
        OutputStream out = null;
        try {
            final File file = getFile();
            if (file != null && !file.exists()) {
                File dir = file.getParentFile();
                if (dir.exists() || dir.mkdirs()) {
                    file.createNewFile();
                }
            }else{
                onFailure(new HttpError("下载地址为空", HttpError.ErrorType.ERROR_FILE_NULL));
                return;
            }

            ProgressResponseBody body = new ProgressResponseBody(response.body(), this);
            in = body.byteStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
                progress += length;
                postDownloadProgress(progress, body.contentLength(), false);
            }
            out.flush();
            postDownloadProgress(progress, body.contentLength(), true);
            postOnMain(new Runnable() {
                @Override
                public void run() {
                    mListener.onResponse(file);
                }
            });
//            postSuccess(request, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            onFailure(new HttpError("文件不存在", HttpError.ErrorType.ERROR_PARSE));
        } catch (IOException e) {
            e.printStackTrace();
            onFailure(new HttpError("IO错误", HttpError.ErrorType.ERROR_PARSE));
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        mListener.onResponse(in);
    }

    @Override
    public final void onResponseProgress(long progress, long contentLength, boolean done) {
//        postDownloadProgress(progress, contentLength, done);
    }

    private int percent = 0;
    public final void postDownloadProgress(final long progress, final long contentLength, final boolean done) {
        int p = (int) (progress*100/contentLength);
        if(p <= percent) {
            return;
        }
        percent = p;
        onDownloadProgress(progress, contentLength, done);

//        ThreadHelper.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                onDownloadProgress(progress, contentLength, done);
//            }
//        });
    }

    public abstract void onDownloadProgress(long progress, long contentLength, boolean done);
}
