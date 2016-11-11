package com.wxdroid.microcodor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.wxdroid.microcodor.model.MsgEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private TextView mText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mText = (TextView) findViewById(R.id.title);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //第2步:注册一个在后台线程执行的方法,用于接收事件
    public void onUserEvent(String event) {//参数必须是ClassEvent类型, 否则不会调用此方法
        Toast.makeText(this,event,Toast.LENGTH_LONG).show();
        mText.setText(event);
    }

}
