package com.wxdroid.basemodule.network.utils;

import java.io.IOException;
import java.io.InputStream;

public class UploadStreamDescriber
{

    public UploadStreamDescriber(InputStream inputStream, long length, String mimeType) {
        this.inputStream = inputStream;
        this.length = length;
        this.mimeType = mimeType;
    }

    public InputStream inputStream;
    public long length;
    public String mimeType;

    public void close() {
        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {

            }
        }
    }
}
