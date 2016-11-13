package com.wxdroid.microcodor.hello;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wxdroid.microcodor.ui.MainActivity;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.model.MsgEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by cjin on 2016/11/12.
 */

public class SplashActivity extends Activity {
    ImageView logo;

    final String json = "Test EventBus";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       /* if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }*/
        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MsgEvent msgEvent = new MsgEvent(json);

            }
        });


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

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
