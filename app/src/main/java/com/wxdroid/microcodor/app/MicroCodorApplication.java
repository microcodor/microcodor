package com.wxdroid.microcodor.app;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/11/15.
 */

public class MicroCodorApplication extends Application {
    private static MicroCodorApplication instance;
    private static ActivityManager activityManager = null; // activity管理类
    private static ExecutorService cachedThreadExecutor;
    public IWXAPI wxapi;

    @Override
    public void onCreate() {
        activityManager = ActivityManager.getInstance(); // 获得实例
        super.onCreate();
        this.instance = this;
        initX5Browser();
        registWechat();
    }

    public static MicroCodorApplication getInstance() {
        return instance;
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    public void initX5Browser() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
                Log.e("app", " onCoreInitFinished");
            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("app", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("app", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("app", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
    /**
     * 创建缓冲线程池，管理线程使用
     */
    public ExecutorService getThreadPool() {
        if (cachedThreadExecutor == null) {
            cachedThreadExecutor = Executors
                    .newCachedThreadPool();
        }
        return cachedThreadExecutor;
    }

    /**
     * 微信分享注册
     * */
    private void registWechat(){
        //初始化并注册微信API
        wxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
        wxapi.registerApp(Constants.WX_APP_ID);
    }
}
