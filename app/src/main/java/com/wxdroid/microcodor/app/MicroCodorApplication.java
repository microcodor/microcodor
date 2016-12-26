package com.wxdroid.microcodor.app;

import android.app.Activity;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.wxdroid.basemodule.GlobalInit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public static boolean isDebug = false;

    @Override
    public void onCreate() {
        activityManager = ActivityManager.getInstance(); // 获得实例
        super.onCreate();
        this.instance = this;
        initCrash();
        initX5Browser();
        registWechat();
        GlobalInit.initImageLoader(getApplicationContext());
    }

    public static MicroCodorApplication getInstance() {
        return instance;
    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    private void initCrash(){
// 获取当前包名
        String packageName = getApplicationContext().getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        Bugly.init(getApplicationContext(), "7199709f4c", isDebug,strategy);
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
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
