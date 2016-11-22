package com.wxdroid.basemodule.network.request;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


public class ProgressRequestBody extends RequestBody
{

    private MediaType contentType;
    private File file;
    private InputStream inputStream;

    private int buflength = 1024;

    private long mProgress;

    private long length;

    public ProgressRequestBody(MediaType contentType, File file) {
        this.contentType = contentType;
        this.file = file;
    }

    /**
     * 响应体进度回调接口，比如用于文件下载中
     */
    public interface ProgressRequestListener {
        void onRequestProgress(long progress, long contentLength, boolean done);
    }

    //进度回调接口
    private ProgressRequestListener mListener;

    public ProgressRequestBody(MediaType contentType, File file, ProgressRequestListener mListener) {
        this.contentType = contentType;
        this.file = file;
        if(file != null) {
            this.length = file.length();
        }
        this.mListener = mListener;
    }

    public ProgressRequestBody(MediaType contentType, InputStream inputStream, long length, ProgressRequestListener mListener) {
        this.contentType = contentType;
        this.inputStream = inputStream;
        this.mListener = mListener;
        this.length = length;
    }

    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException
    {
        Source source = null;
        try {
            mProgress = 0;
            if(file != null && file.exists()) {
                source = Okio.source(file);
            }
            if(inputStream != null) {
                source = Okio.source(inputStream);
            }
            if(source != null) {
                //sink.writeAll(source);
                Buffer buffer = new Buffer();
                long len = 0;
                while ((len = source.read(buffer, buflength)) != -1) {
                    sink.write(buffer, len);
                    if (mListener != null) {
                        mProgress += len;
                        mListener.onRequestProgress(mProgress, contentLength(), mProgress == contentLength());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long contentLength() throws IOException
    {
        return length;
    }
}
