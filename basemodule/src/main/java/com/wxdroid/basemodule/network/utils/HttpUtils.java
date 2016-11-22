package com.wxdroid.basemodule.network.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import java.util.Comparator;
import java.util.Map;

public class HttpUtils {
    /**
     * 排序
     */
    private static Comparator<String> htmlComp = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);// 按字典顺序去比较两个字符串
        }
    };

    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;
    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 3;
    /** 4G和4G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_4G = 5;
    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 4;

    private static int isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return 0; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return 0; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return 0; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return 1; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return 1; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return 0; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return 1; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return 1; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return 1; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return 1; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return 1; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return 1; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return 1; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return 0; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return 2; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return 0;
            default:
                return 2;
        }
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @param context
     *            上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
     *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
     *         <p>
     *         {@link #NETWORKTYPE_WIFI}
     */

    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();

                int mobile = NETWORKTYPE_2G;
                switch(isFastMobileNetwork(context)) {
                    case 0:
                        mobile = NETWORKTYPE_2G;
                        break;
                    case 1:
                        mobile = NETWORKTYPE_3G;
                        break;
                    case 2:
                        mobile = NETWORKTYPE_4G;
                        break;
                }
                return (proxyHost == null || proxyHost.length() == 0) ? /*(isFastMobileNetwork(context) ? NETWORKTYPE_3G
						: NETWORKTYPE_2G)*/mobile
                        : NETWORKTYPE_WAP;
            }
        }

        return NETWORKTYPE_INVALID;
    }

    public static boolean isNetWorkEnable(Context context) {
        boolean flag = false;
        int netStatus = HttpUtils.getNetWorkType(context);
        if (netStatus == HttpUtils.NETWORKTYPE_INVALID || netStatus == HttpUtils.NETWORKTYPE_2G) {
            flag = false;
        } else {
            flag = true;
        }

        return flag;
    }

    public static boolean isNetWorkWifiType(Context context) {
        if (NETWORKTYPE_WIFI == getNetWorkType(context)) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取网络类型
     * @param context
     * @return
     */
    public static String getNetWorkTypeToString(Context context) {
        switch (getNetWorkType(context)) {
            case NETWORKTYPE_WIFI:
                return "wifi";
            case NETWORKTYPE_WAP:
                return "wap";
            case NETWORKTYPE_2G:
                return "2g";
            case NETWORKTYPE_3G:
                return "3g";
            case NETWORKTYPE_4G:
                return "4g";
        }
        return "unkown";
    }

    public static String formatUrlByMethod(String url, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            Uri.Builder builder = Uri.parse(url).buildUpon();
            for (String key : params.keySet()) {
                if (!TextUtils.isEmpty(key)) {
                    String value = params.get(key);
                    if (TextUtils.isEmpty(value)) {
                        value = "";
                    }
                    builder.appendQueryParameter(key, value);
                }
            }
            return builder.toString();
        }
        return url;
    }
}
