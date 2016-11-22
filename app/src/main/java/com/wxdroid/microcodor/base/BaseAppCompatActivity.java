package com.wxdroid.microcodor.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.ui.SearchActivity;

/**
 * Created by jinchun on 2016/11/19.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {
    public Activity activity;

    public final static String TAG = BaseAppCompatActivity.class.getSimpleName();
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        this.activity = this;

        mToolbar = getToolbar();
        /*
          toolbar.setLogo(R.mipmap.ic_launcher);
          toolbar.setTitle("Title");
          toolbar.setSubtitle("Sub Title");
          */
        mToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }
        if (mToolbarTitle != null) {
            //getTitle()的值是activity的android:lable属性值
            mToolbarTitle.setText(getTitle());
            ActionBar ab=getSupportActionBar();
            if (ab!=null) {
                //给左上角图标的左边加上一个返回的图标
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ///使左上角图标是否显示
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                //决定左上角的图标是否可以点击
                getSupportActionBar().setHomeButtonEnabled(true);
                //设置默认的标题不显示
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
        setupView();
        setupData();

    }
    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     *
     * @return res layout xml id
     */
    protected abstract int setLayoutId();

    protected  abstract void setupView();

    protected abstract void setupData();

    protected abstract void setChouTi();

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getToolbarTitle() {
        return mToolbarTitle;
    }

    /**
     * 获取头部标题的TextView
     *
     * @return
     */
    public TextView getSubTitle() {
        return mToolbarSubTitle;
    }

    /**
     * 设置头部标题
     *
     * @param title
     */
    public void setToolBarTitle(CharSequence title) {
        if (mToolbarTitle != null) {
            mToolbarTitle.setText(title);
        } else {
            getToolbar().setTitle(title);
            setSupportActionBar(getToolbar());
        }
    }

    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    public void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void showChouTi() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        getToolbar().setNavigationIcon(R.drawable.ic_menu_white_24dp);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChouTi();
            }
        });

    }



    public void showVisible(boolean flag){
        getToolbar().getMenu().findItem(R.id.action_search).setVisible(flag);
        supportInvalidateOptionsMenu();
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }


    /**
     * 如果需要隐藏右侧菜单栏，只需重写此方法，不去布局menu_main即可
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                //setIntentClass(SearchActivity.class);
                startActivity(activity, SearchActivity.class);
                break;
            case R.id.action_setting:
                //testSnackbar();
                break;
        }
        return true;
    }

    /**
     * Activity的跳转
     */
    public void startActivity(Activity activity, Class<?> cla) {
        Intent intent = new Intent();
        intent.setClass(activity, cla);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }
}
