package com.wxdroid.microcodor.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wxdroid.microcodor.MainActivity;
import com.wxdroid.microcodor.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjin on 2016/11/12.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);

        final String json="Test EventBus";


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                EventBus.getDefault().post(json);
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
            }
        },3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
