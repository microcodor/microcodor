package com.wxdroid.microcodor.base;

/**
 * Created by jinchun on 2016/11/29.
 */

public class BaseModel {
    /**
     * msg : success
     * status : 0
     */

    public int status;
    public String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
