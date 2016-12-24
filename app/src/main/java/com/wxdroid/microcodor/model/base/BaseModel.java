package com.wxdroid.microcodor.model.base;

/**
 * Created by jinchun on 2016/11/29.
 */

public class BaseModel {
    /**
     * msg : success
     * status : 0
     */

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "status='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
