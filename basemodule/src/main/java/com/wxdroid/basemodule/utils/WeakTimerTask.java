package com.wxdroid.basemodule.utils;

import java.lang.ref.WeakReference;
import java.util.TimerTask;

public class WeakTimerTask extends TimerTask
{
    public interface ITimerTask
    {
        void TimerTaskRunnable();
    }

    WeakReference<ITimerTask> timer_task_imp;
    public WeakTimerTask(ITimerTask imp)
    {
        timer_task_imp = new WeakReference<>(imp);
    }
    @Override
    public void run()
    {
        if (timer_task_imp != null && timer_task_imp.get() != null)
        {
            timer_task_imp.get().TimerTaskRunnable();
        }
    }
}
