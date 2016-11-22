package com.wxdroid.basemodule.network.utils;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadHelper
{

    private static final ExecutorService backgroundTasksExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new PriorityThreadFactory());

    public static void runOnUiThread(Runnable action) {
        ThreadUtils.runOnUiThread(action);
    }

    public static final boolean threadInMain() {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId())
            return true;
        return false;
    }

    public static Future<?> runOnBackgroundThread(Runnable action) {
        if (action == null)
            return null;

        return backgroundTasksExecutor.submit(action);
    }
}
