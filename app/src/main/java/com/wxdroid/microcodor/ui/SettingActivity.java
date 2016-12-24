package com.wxdroid.microcodor.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.wxdroid.microcodor.R;
import com.wxdroid.microcodor.app.Constants;
import com.wxdroid.microcodor.base.BaseAppCompatActivity;

/**
 * 设置页面
 * Created by jinchun on 2016/12/9.
 */

public class SettingActivity extends BaseAppCompatActivity implements View.OnClickListener{
    private TextView version_textview;
    private TextView website_btn;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void setupView() {
        setToolBarTitle(getString(R.string.text_about));
        showBack();
        version_textview = (TextView) findViewById(R.id.version_textview);
        website_btn = (TextView) findViewById(R.id.website_btn);
        website_btn.setOnClickListener(this);
    }

    @Override
    protected void setupData() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            if (info!=null){
                version_textview.setText(info.versionName+"."+info.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.website_btn:
                Uri uri = Uri.parse(Constants.APP_WEBSITE_URL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }
}
