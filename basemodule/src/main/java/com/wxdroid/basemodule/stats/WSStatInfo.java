package com.wxdroid.basemodule.stats;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;

@SuppressLint("CommitPrefEdits")
public class WSStatInfo
{
  private static SharedPreferences sp = null;
  private static SharedPreferences.Editor spEditor = null;
  public static final String INFO_FILE = "hfstats_data_analysis";
  
  static final String KEY_LOG_TIME = "time";
  static final String KEY_ID = "id";
  static final String KEY_UID = "uid";
  static final String KEY_DEVICE_ID = "did";
  static final String KEY_APP_VERSION = "appversion";
  static final String KEY_NET = "net";
  static final String KEY_OS = "os";
  static final String KEY_OS_VERSION = "osversion";
  static final String KEY_RESOLUTION = "resolution";
  static final String KEY_MODEL = "model";
  static final String KEY_EVENT = "event";
  static final String KEY_VALUE = "value";
  static final String KEY_COUNT = "count";
  static final String KEY_CARRIER = "carrier";
  
  static final String KEY_USER_PAUSE_TIME = "user_pause_time";
  static final String KEY_LAST_COMMIT_TIME = "last_commit_time";
  static final String KEY_USER_DURATION = "user_duration";
 //static final String KEY_SDK_VERSION = "sdk_ver";
  static final String KEY_CHANNEL = "channel";
  
  static synchronized void init(Context context)
  {
    if (sp == null)
    {
      sp = context.getSharedPreferences(INFO_FILE, 0);
      spEditor = sp.edit();
    }
  }
  
  static void updateAppAndDeviceInfo(WSDatabaseManager dbManager, Context activity)
  {
    try
    {
    	String deviceId = WSUtil.getDeviceId(activity);
        putString(KEY_DEVICE_ID, deviceId);
    }
    catch (Exception e) {}
    
    putString(KEY_MODEL, Build.MODEL);

    if(activity instanceof Activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) activity).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        putString(KEY_RESOLUTION, displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
    }

  /*  
    String sdkVersion = getString(KEY_SDK_VERSION, null);
    if (!HFStats.SDK_VERSION.equals(sdkVersion))
    {
      putString(KEY_SDK_VERSION, HFStats.SDK_VERSION);
      if (sdkVersion != null) {
        WSStatEvent.addEvent(dbManager, WSStatEvent.EVENT_UPDATE, date, hour, KEY_SDK_VERSION);
      }
    }
  */
    String channel = getString(KEY_CHANNEL, null);
    if (channel == null) {
    	channel = WSUtil.getChannel(activity);
    }   
    putString(KEY_CHANNEL, channel);

    putString(KEY_APP_VERSION, WSUtil.getVersionName(activity));
    putString(KEY_OS_VERSION, Build.VERSION.RELEASE);
   
    String carrier = WSUtil.getCarrier(activity);
    if (!carrier.equals(""))
    {
        putString(KEY_CARRIER, carrier);
    }
  }
  
  static void putString(String key, String value)
  {
    spEditor.putString(key, value);
    spEditor.commit();
  }
  
  public static String getString(String key, String def)
  {
    return sp.getString(key, def);
  }
  
  static void putInt(String key, int value)
  {
    spEditor.putInt(key, value);
    spEditor.commit();
  }
  
  static int getInt(String key, int def)
  {
    return sp.getInt(key, def);
  }
  
  static void putLong(String key, long value)
  {
    spEditor.putLong(key, value);
    spEditor.commit();
  }
  
  static long getLong(String key, long def)
  {
    return sp.getLong(key, def);
  }
  
  static void putBoolean(String key, boolean value)
  {
    spEditor.putBoolean(key, value);
    spEditor.commit();
  }
  
  static boolean getBoolean(String key, boolean def)
  {
    return sp.getBoolean(key, def);
  }
}