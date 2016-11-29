package com.wxdroid.microcodor.ui;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wxdroid.basemodule.EventBusManager;
import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.base.BaseAdapterHelper;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;
import com.wxdroid.microcodor.base.BaseQuickAdapter;
import com.wxdroid.microcodor.model.WpTermModel;
import com.wxdroid.microcodor.model.WptermsBean;
import com.wxdroid.microcodor.network.NetWorksUtils;
import com.wxdroid.microcodor.widget.QuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView mText;
    private LinearLayout rootView;

    private Button wxdroidBtn;

    private RecyclerView mDrawerList;
    private QuickAdapter<WpTermModel> mQuickAdapter;

    List<WpTermModel> wpTermModelList = new ArrayList<>();

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


    }

    private void initLeftDrawer(){
        mDrawerList = (RecyclerView) findViewById(R.id.left_recyclerview);
        mDrawerList.setHasFixedSize(true);

        mQuickAdapter = new QuickAdapter<WpTermModel>(this, R.layout.item_code_type, wpTermModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, WpTermModel item) {
                helper.getTextView(R.id.code_name).setText(item.getName() + "");
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
        getAllClassifies();
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

    /**
     * 获取所有分类
     */
    private void getAllClassifies() {
        NetWorksUtils.GetWpterms(new Observer<WptermsBean>() {
            @Override
            public void onCompleted() {
                Log.d("onCompleted", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                //异常
                Log.e("MAIN3", e.getLocalizedMessage() + "--" + e.getMessage());
            }

            @Override
            public void onNext(WptermsBean wptermBean) {
                //成功
                Log.d("onNext", "" + wptermBean.getStatus() + ";" + wptermBean.getMsg());
                if (wptermBean.getStatus() == 200) {
                    wpTermModelList = wptermBean.getData();
                    initLeftDrawer();

                    //mQuickAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
