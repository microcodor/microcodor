package com.wxdroid.microcodor.model.bean;

import com.wxdroid.microcodor.model.base.BaseModel;
import com.wxdroid.microcodor.model.WpPostsModel;

import java.io.Serializable;

/**
 * Created by jinchun on 2016/12/2.
 */
//@DatabaseTable(tableName = "wp_posts")
public class WpPostsModelBean implements Serializable {
    private BaseModel common;
    private WpPostsModel data;

    public BaseModel getCommon() {
        return common;
    }

    public void setCommon(BaseModel common) {
        this.common = common;
    }

    public WpPostsModel getData() {
        return data;
    }

    public void setData(WpPostsModel data) {
        this.data = data;
    }
}
