package com.wxdroid.microcodor.model.bean;

import com.wxdroid.microcodor.base.BaseModel;
import com.wxdroid.microcodor.model.WpPostsModel;

import java.util.List;

/**
 * Created by jinchun on 2016/12/9.
 */

public class WpPostsModelListBean {
    private BaseModel common;
    private List<WpPostsModel> data;

    public BaseModel getCommon() {
        return common;
    }

    public void setCommon(BaseModel common) {
        this.common = common;
    }

    public List<WpPostsModel> getData() {
        return data;
    }

    public void setData(List<WpPostsModel> data) {
        this.data = data;
    }
}
