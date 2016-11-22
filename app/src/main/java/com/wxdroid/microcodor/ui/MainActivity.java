package com.wxdroid.microcodor.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wxdroid.basemodule.EventBusManager;
import com.wxdroid.basemodule.network.HttpError;
import com.wxdroid.basemodule.network.HttpRequest;
import com.wxdroid.basemodule.network.request.StringRequesetListener;
import com.wxdroid.basemodule.network.request.StringRequest;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.base.BaseAdapterHelper;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;
import com.wxdroid.microcodor.base.BaseQuickAdapter;
import com.wxdroid.microcodor.util.LogUtil;
import com.wxdroid.microcodor.widget.QuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView mText;
    private LinearLayout rootView;

    private Button wxdroidBtn;

    private RecyclerView mDrawerList;
    private QuickAdapter mQuickAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        rootView = (LinearLayout) findViewById(R.id.activity_main);
//        //mText = (TextView) findViewById(R.id.title);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        //toolbar.setTitle("微码农");
//        setSupportActionBar(toolbar);
//        toolbar.inflateMenu(R.menu.menu_main);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wxdroidBtn = (Button) findViewById(R.id.btn_wxdroid);
        wxdroidBtn.setOnClickListener(this);

        setToolBarTitle(getResources().getString(R.string.app_name));
        showChouTi();
        setupDrawer();

        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        mDrawerList.setHasFixedSize(true);
        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("Android");
        typeList.add("IOS");
        mQuickAdapter = new QuickAdapter<String>(this, R.layout.item_code_type, typeList) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.getTextView(R.id.code_name).setText(item + "");
            }
        };
        mDrawerList.setAdapter(mQuickAdapter);
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectCodeItem(position);
            }
        });
    }

    private void selectCodeItem(int position) {
        testSnackbar("position:" + position);
        drawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void setupData() {

        EventBusManager.getInstance().register(this);
        testhttp();
    }

    @Override
    protected void setChouTi() {
        testSnackbar("首页显示抽屉效果");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void testSnackbar(String msg) {
        Snackbar.make(rootView, "" + msg, Snackbar.LENGTH_INDEFINITE)
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
        EventBusManager.getInstance().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN) //第2步:注册一个在后台线程执行的方法,用于接收事件
    public void onUserEvent(String event) {//参数必须是ClassEvent类型, 否则不会调用此方法
        Toast.makeText(this, event, Toast.LENGTH_LONG).show();
        //mText.setText(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_wxdroid:
                startActivity(new Intent(MainActivity.this, WxdroidActivity.class));
                break;
        }
    }

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, getToolbar(), R.string.app_name, R.string.main_name);
        mDrawerToggle.syncState();
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void testhttp(){
        String url = "http://172.16.105.22/api/index.php?username=张三&format=json";
        //url = "http://115.159.204.29/gety8u";
        StringRequest stringRequest = new StringRequest(HttpRequest.HttpMethod.GET, url, new StringRequesetListener() {
            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG,"onResponse:"+response);
            }

            @Override
            public void onFailure(HttpError e) {
                LogUtil.d(TAG,"onResponse-onFailure:"+e.getMessage());
            }
        });
        stringRequest.execute();
    }
}
