package com.wxdroid.microcodor.model.bean;

import com.wxdroid.microcodor.base.BaseModel;
import com.wxdroid.microcodor.model.WpTermModel;

import java.util.List;

/**
 * Created by jinchun on 2016/11/29.
 */

public class WptermsBean{

    private BaseModel common;

    private List<WpTermModel> data;

    public BaseModel getCommon() {
        return common;
    }

    public void setCommon(BaseModel common) {
        this.common = common;
    }

    public List<WpTermModel> getData() {
        return data;
    }

    public void setData(List<WpTermModel> data) {
        this.data = data;
    }

}
