package com.wxdroid.microcodor.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.webview.utils.X5WebView;

/**
 * Created by jinchun on 2016/11/16.
 */

public class WxdroidActivity extends AppCompatActivity {
    private X5WebView mWebView;

    private static String mUrl = "http://wxdroid.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxdroid);

        mWebView = (X5WebView) findViewById(R.id.webview);

        mWebView.loadUrl(mUrl);
    }
}
