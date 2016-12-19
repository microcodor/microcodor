package com.wxdroid.microcodor.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
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
import com.wxdroid.microcodor.app.MicroCodorApplication;
import com.wxdroid.microcodor.base.BaseAdapterHelper;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;
import com.wxdroid.microcodor.base.BaseQuickAdapter;
import com.wxdroid.microcodor.model.WpTermModel;
import com.wxdroid.microcodor.model.bean.WpPostsModelBean;
import com.wxdroid.microcodor.model.bean.WpPostsModelListBean;
import com.wxdroid.microcodor.model.bean.WptermsBean;
import com.wxdroid.microcodor.model.service.WpTermModelService;
import com.wxdroid.microcodor.network.NetWorksUtils;
import com.wxdroid.microcodor.ui.fragment.CommonFragment;
import com.wxdroid.microcodor.util.LogUtil;
import com.wxdroid.microcodor.widget.QuickAdapter;
import com.wxdroid.microcodor.widget.RecycleViewDivider;

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

    List<Fragment> fragmentList = new ArrayList<>();
    FragmentTransaction fragmentTransaction;

    private boolean termsIsExistDatabase = false;

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
//        wxdroidBtn = (Button) findViewById(R.id.btn_wxdroid);
//        wxdroidBtn.setOnClickListener(this);

        setToolBarTitle(getResources().getString(R.string.app_name));
        showChouTi();
        setupDrawer();


    }

    private void initLeftDrawer(){
        mDrawerList = (RecyclerView) findViewById(R.id.left_recyclerview);
        mDrawerList.setHasFixedSize(true);
        mDrawerList.addItemDecoration(new RecycleViewDivider());

        mQuickAdapter = new QuickAdapter<WpTermModel>(this, R.layout.item_code_type, wpTermModelList) {
            @Override
            protected void convert(BaseAdapterHelper helper, WpTermModel item) {
                helper.getTextView(R.id.code_head).setText(item.getName().substring(0,1).toUpperCase());
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
        selectItem(position);
        drawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void setupData() {
        EventBusManager.getInstance().register(this);
        wpTermModelList = WpTermModelService.queryAllWpTermModelList();
        LogUtil.d(TAG,"wpTermModelList:"+wpTermModelList.size());
        if (wpTermModelList!=null&wpTermModelList.size()>0){
            termsIsExistDatabase = true;
            initLeftDrawer();
            selectItem(0);
        }else {
            termsIsExistDatabase = false;
        }
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
//            case R.id.btn_wxdroid:
//                startActivity(new Intent(MainActivity.this, WxdroidActivity.class));
//
//                break;
        }
    }

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    private void setupDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, getToolbar(), R.string.app_name, R.string.main_name);
        //mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerToggle.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu_white_24dp));
        drawerLayout.setDrawerListener(mDrawerToggle);
    }
    private void selectItem(int position) {
        setToolBarTitle(wpTermModelList.get(position).getName());
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragmentList.size()==0) {
            initFragments();
        }
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(fragmentTransaction);
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i == position) {
                fragmentTransaction.show(fragmentList.get(i));
                break;
            }
        }
        fragmentTransaction.commit();

    }
    private void hideFragments(FragmentTransaction transaction) {
        for (Fragment fragment:fragmentList){
            transaction.hide(fragment);
        }
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
                Log.d("onNext", "" + wptermBean.getCommon().getCode() + ";" + wptermBean.getCommon().getMsg());
                Log.d("onNext", "size:" +wptermBean.getData().size());
                if (wptermBean.getCommon().getCode() == 1) {
                    wpTermModelList = wptermBean.getData();
                    MicroCodorApplication.getInstance().getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            WpTermModelService.saveOrUpdateWpTermModelsList(wpTermModelList);
                        }
                    });
                    if (!termsIsExistDatabase) {
                        initLeftDrawer();
                        selectItem(0);
                    }
                    //mQuickAdapter.notifyDataSetChanged();
                }
            }
        });
    }



    /**
     * 初始化fragments
     *
     * */
    private void initFragments(){
        // 开启一个Fragment事务
        if (wpTermModelList!=null&&wpTermModelList.size()>0){
            for (int i=0;i<wpTermModelList.size();i++){
                Fragment fragment = new CommonFragment();
                Bundle bundle = new Bundle();
                bundle.putLong("termId", wpTermModelList.get(i).getTerm_id());
                fragment.setArguments(bundle);

                fragmentList.add(fragment);
                fragmentTransaction.add(R.id.content_frame,fragmentList.get(i));
            }

        }
    }
}
