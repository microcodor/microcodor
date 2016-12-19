package com.wxdroid.microcodor.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wxdroid.microcodor.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjin on 2016/11/12.
 */

public class SplashActivity extends Activity {
    private SimpleDraweeView dvWelcome;

    final String json = "Test EventBus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//放在加载布局之前
        setContentView(R.layout.activity_splash);

        dvWelcome= (SimpleDraweeView) findViewById(R.id.dv_welcome);
        /**
         * 下面是主要代码：
         */
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)//自动播放动画
                .setUri(Uri.parse("asset://com.wxdroid.microcodor/gif/terminal.gif"))//路径
                .build();
        dvWelcome.setController(draweeController);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().postSticky(json);
    }
}
