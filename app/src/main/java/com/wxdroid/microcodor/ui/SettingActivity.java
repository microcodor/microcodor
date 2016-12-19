package com.wxdroid.microcodor.ui;

import android.view.Menu;
import android.view.MenuItem;

import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;

/**
 * 设置页面
 * Created by jinchun on 2016/12/9.
 */

public class SettingActivity extends BaseAppCompatActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupView() {
        setToolBarTitle("设置");
        showBack();
    }

    @Override
    protected void setupData() {

    }

    @Override
    protected void setChouTi() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }
}
