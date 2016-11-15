package com.wxdroid.microcodor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.webview.utils.X5WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mText;
    private LinearLayout rootView;

    private Button wxdroidBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        rootView = (LinearLayout) findViewById(R.id.activity_main);
        mText = (TextView) findViewById(R.id.title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //toolbar.setTitle("微码农");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wxdroidBtn = (Button) findViewById(R.id.btn_wxdroid);
        wxdroidBtn.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_setting:
                testSnackbar();
                break;
        }

        return true;
    }
    private void testSnackbar(){
        Snackbar.make(rootView, "您有新短消息，请注意查收。", Snackbar.LENGTH_INDEFINITE)
                .setAction("点击查看", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "TODO 查看消息", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //第2步:注册一个在后台线程执行的方法,用于接收事件
    public void onUserEvent(String event) {//参数必须是ClassEvent类型, 否则不会调用此方法
        Toast.makeText(this,event, Toast.LENGTH_LONG).show();
        mText.setText(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_wxdroid:
                startActivity(new Intent(MainActivity.this,WxdroidActivity.class));
                break;
        }
    }
}
