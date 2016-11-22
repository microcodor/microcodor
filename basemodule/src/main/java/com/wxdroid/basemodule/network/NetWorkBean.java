package com.wxdroid.basemodule.network;

import android.os.Parcel;
import android.os.Parcelable;

public class NetWorkBean implements Parcelable
{
    /**
     * 网络状态
     * NET_UNKNOWN：未知网络
     * NET_NO：没有网络
     * NET_2G:2g网络
     * NET_3G：3g网络
     * NET_4G：4g网络
     * NET_WIFI：wifi
     */
    public interface NetState{
        public  final int NET_UNKNOWN = -1;
        public  final int NET_NO = 0;
        public  final int NET_2G = 1;
        public  final int NET_3G = 2;
        public  final int NET_4G = 3;
        public  final int NET_WIFI = 4;

      }


    public int state = NetState.NET_UNKNOWN;
    public String name;//以备扩展，2G\3G\4G\WIFI\

    public NetWorkBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state);
        dest.writeString(this.name);
    }

    protected NetWorkBean(Parcel in) {
        this.state = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<NetWorkBean> CREATOR = new Creator<NetWorkBean>() {
        public NetWorkBean createFromParcel(Parcel source) {
            return new NetWorkBean(source);
        }

        public NetWorkBean[] newArray(int size) {
            return new NetWorkBean[size];
        }
    };

    public static String getNetNameByState(int state){
        String netName = "未知网路";
        switch (state) {
            case NetState.NET_UNKNOWN:
                netName = "未知网路";
                break;
            case NetState.NET_NO:
                netName = "没有网络";
                break;
            case NetState.NET_2G:
                netName = "2g网络";
                break;
            case NetState.NET_3G:
                netName = "3g网络";
                break;
            case NetState.NET_4G:
                netName = "4g网络";
                break;
            case NetState.NET_WIFI:
                netName = "WIFI网络";
                break;
        }
        return netName;
    }

    public boolean isWiFi(){
        return state == NetState.NET_WIFI;
    }

    public interface NetWorkObserver{
         NetWorkBean getNetWorkBean();
    }
}
