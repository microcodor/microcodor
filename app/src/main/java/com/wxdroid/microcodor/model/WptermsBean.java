package com.wxdroid.microcodor.model;

import com.wxdroid.microcodor.base.BaseModel;

import java.util.List;

/**
 * Created by jinchun on 2016/11/29.
 */

public class WptermsBean extends BaseModel {


    private List<WpTermModel> data;


    public List<WpTermModel> getData() {
        return data;
    }

    public void setData(List<WpTermModel> data) {
        this.data = data;
    }

}
