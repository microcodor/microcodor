package com.wxdroid.basemodule.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

    private static String imei;
    private static String deviceid;

    /**
     * 获取deviceid
     *
     * @return
     */
    public static String getDeviceId(Context context) {
//        if(TextUtils.isEmpty(deviceid)) {
//            final TelephonyManager tm = (TelephonyManager) BaseApplication.getContext()
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//
//            final String tmDevice, tmSerial, androidId;
//            tmDevice = "" + tm.getDeviceId();
//            tmSerial = "" + tm.getSimSerialNumber();
//            androidId = ""
//                    + android.provider.Settings.Secure.getString(
//                    BaseApplication.getContext().getContentResolver(),
//                    android.provider.Settings.Secure.ANDROID_ID);
//
//            UUID deviceUuid = new UUID(androidId.hashCode(),
//                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//            String deviceId = deviceUuid.toString();
//            imei = tmDevice;
//            deviceid = SecurityUtils.encryptMD5(deviceId).toLowerCase();
//        }
//        return deviceid;
        return getM2(context);
    }

    /**
     * 获取IMEI 值
     *
     * @return
     */
    public static String getImei(Context context) {
        if (TextUtils.isEmpty(imei)) {
            final TelephonyManager tm =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        }
        return imei;
    }

    /**
     * 获取IMEI md5值
     *
     * @return
     */
    public static String getImeiMd5(Context context) {
        return SecurityUtils.encryptMD5(getImei(context)).toLowerCase();
    }

    /**
     * 安装apk
     *
     * @param path
     */
    public static void installApk(String path, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param resStrId
     * @param resColorId
     * @param object
     * @param colorRun   第几个生效
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilder(Context context, int resStrId, int resColorId, int colorRun, String... object) {
        String processScanReport = context.getString(resStrId, (Object[]) object);

        int len = object.length;
        int index[] = new int[len];
        String oldStr = "";// 计算过的距离，解决有时候object内容一样出现的问题
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                int s = processScanReport.indexOf(object[i]);
                index[i] = s;
                oldStr = processScanReport.substring(s + 1, processScanReport.length());
            } else {
                int s = oldStr.indexOf(object[i]);
                index[i] = s + index[i - 1] + 1;
                oldStr = oldStr.substring(s + 1, oldStr.length());
            }
            // index[i] = processScanReport.indexOf(object[i]);
        }

        SpannableStringBuilder processSpanStr = new SpannableStringBuilder(processScanReport);
        //2016/4/8
     /*   for (int i = 0; i < len; i++) {
            // index[i] = processScanReport.indexOf(object[i]);
            if (colorRun == -1 || colorRun == i) {
                processSpanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(resColorId)), index[i],
                        index[i] + object[i].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                processSpanStr.setSpan(new StyleSpan(Typeface.BOLD), index[i], index[i] + object[i].length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }*/
        return processSpanStr;
    }

    /**
     * 动态替换文本并改变文本颜色
     *
     * @param context
     * @param resStr
     * @param resColorId
     * @param object
     * @param colorRun   第几个生效
     * @return
     */
    public static SpannableStringBuilder getSpannableStringBuilder(Context context, String resStr, int resColorId, int colorRun, String... object) {
        String processScanReport = String.format(resStr, (Object[]) object);//context.getString(resStrId, (Object[]) object);

        int len = object.length;
        int index[] = new int[len];
        String oldStr = "";// 计算过的距离，解决有时候object内容一样出现的问题
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                int s = processScanReport.indexOf(object[i]);
                index[i] = s;
                oldStr = processScanReport.substring(s + 1, processScanReport.length());
            } else {
                int s = oldStr.indexOf(object[i]);
                index[i] = s + index[i - 1] + 1;
                oldStr = oldStr.substring(s + 1, oldStr.length());
            }
            // index[i] = processScanReport.indexOf(object[i]);
        }

        SpannableStringBuilder processSpanStr = new SpannableStringBuilder(processScanReport);
        //2016/4/8
       /* for (int i = 0; i < len; i++) {
            // index[i] = processScanReport.indexOf(object[i]);
            if (colorRun == -1 || colorRun == i) {
                processSpanStr.setSpan(new ForegroundColorSpan(context.getResources().getColor(resColorId)), index[i],
                        index[i] + object[i].length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                processSpanStr.setSpan(new StyleSpan(Typeface.BOLD), index[i], index[i] + object[i].length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }*/
        return processSpanStr;
    }

    /**
     * 计算大数字显示
     *
     * @param num
     * @return
     */
    public static String getFormatUserNum(int num) {
        String numStr = "";
        DecimalFormat dcmFmt = new DecimalFormat("0.0");
        if (num < 1000) {
            numStr = String.valueOf(num);
        } else if (num >= 1000 && num < 10000) {
            float showTotal = (float) num / 1000f;
            numStr = String.valueOf(dcmFmt.format(showTotal)) + "k";
        } else if (num >= 10000) {
            float showTotal = (float) num / 10000f;
            numStr = String.valueOf(dcmFmt.format(showTotal)) + "w";
        }
        return numStr;
    }

    /**
     * 获取包签名md5
     *
     * @return
     */
    public static String getPackageSign(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (info != null && info.signatures != null) {
                Signature sig = info.signatures[0];
                byte[] sigHash = SecurityUtils.MD5(sig.toByteArray());
//                if (AppEnv.DEBUG) {
//                }

                return SecurityUtils.encryptMD5(new byte[]{
                        sigHash[0], sigHash[1], sigHash[2], sigHash[3]
                });
            }
        } catch (PackageManager.NameNotFoundException e) {
        }

        return null;
    }

    /**
     * 获取apk包名
     *
     * @param apkPath
     * @return
     */
    public static String getPackageNameByPath(String apkPath, Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        return packageInfo.packageName;
    }

    /**
     * 获取apk包签名 md5
     *
     * @param apkPath
     * @return
     */
    public static String getPackageSignByPath(String apkPath, Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_SIGNATURES);
        if (packageInfo.signatures != null) {
            Signature sig = packageInfo.signatures[0];
            byte[] sigHash = SecurityUtils.MD5(sig.toByteArray());
            return SecurityUtils.encryptMD5(new byte[]{sigHash[0], sigHash[1], sigHash[2], sigHash[3]});
        }
        return null;
    }

    /**
     * byte 转化成MB KB
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "  MB ");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "  KB ");
    }

    /**
     * 隐藏手机号中间四位
     *
     * @param phoneNum
     * @return
     */
    public static String getFormatPhoneNum(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum) && phoneNum.length() == 11) {
            return phoneNum.substring(0, 3) + "****" + phoneNum.substring(7, phoneNum.length());
        }
        return phoneNum;
    }


    public static void HideImm(View edit) {
        InputMethodManager imm = (InputMethodManager) edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    public static void ShowImm(View edit) {
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) edit.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (edit.isFocused()) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            edit.invalidate();
        }
    }

    /**
     * 获取通知栏管理类
     *
     * @return
     */
    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 清空通知栏
     */
    public static void cancelAllNotification(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    /**
     * 清理单个通知栏
     *
     * @param id
     */
    public static void cancelNotification(int id, Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(id);
    }

    /**
     * 清理单个通知栏
     *
     * @param tag
     * @param id
     */
    public static void cancelNotification(String tag, int id, Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(tag, id);
    }

//    /**
//     * 隐藏键盘
//     */
//    public static void hideSoftInput(Activity context) {
//        if (context == null) {
//            return;
//        }
//        try {
//            View focusView = context.getCurrentFocus();
//            if (focusView != null) {
//                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                        focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 强制隐藏键盘
     */
    public static void hideSoftInputForce(Activity context, IBinder windowToken) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                windowToken, 0);
    }

    /**
     * 弹出软键盘
     */
    public static boolean showSoftInput(Context context, EditText editText) {
        try {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            return inputManager.showSoftInput(editText, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断list是否为空
     */
    public static boolean isListEmpty(List<?> list) {
        if (null == list || 0 >= list.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断Queue是否为空
     */
    public static boolean isQueueEmpty(Queue<?> queue) {
        if (null == queue || 0 >= queue.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断Queue是否不为空
     */
    public static boolean isQueueNotEmpty(Queue<?> queue) {
        return !isQueueEmpty(queue);
    }

    /**
     * 获得list的大小
     */
    public static int getListSize(List<?> list) {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    /**
     * 获得HashTable的大小
     */
    public static int getListSize(Hashtable<?, ?> list) {
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    /**
     * 判断list是否不为空
     */
    public static boolean isListNotEmpty(List<?> list) {
        return !isListEmpty(list);
    }

    /**
     * 判断map是否为空
     */
    public static boolean isMapEmpty(Map<?, ?> map) {
        if (null == map || 0 >= map.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断SparseArray是否为空
     */
    public static boolean isSparseArrayEmpty(SparseArray<?> map) {
        if (null == map || 0 >= map.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断SparseArray是否不为空
     */
    public static boolean isSparseArrayNotEmpty(SparseArray<?> map) {
        return !isSparseArrayEmpty(map);
    }

    /**
     * SparseArray的大小
     */
    public static int getSparseArraySize(SparseArray<?> map) {
        if (null == map) {
            return 0;
        }
        return map.size();
    }

    /**
     * 判断map是否不为空
     */
    public static boolean isMapNotEmpty(Map<?, ?> map) {
        return !isMapEmpty(map);
    }

    /**
     * SparseArray是否为空
     */
    public static boolean isSpareArrayEmpty(SparseArray<?> array) {
        if (null == array || 0 >= array.size()) {
            return true;
        }
        return false;
    }

    /**
     * 判断SparseArray是否不为空
     */
    public static boolean isSpareArrayNotEmpty(SparseArray<?> array) {
        return !isSpareArrayEmpty(array);
    }


    public static String getCurrentActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);


        // get the info from the currently running task
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);


        ComponentName componentInfo = taskInfo.get(0).topActivity;
        return componentInfo.getClassName();
    }


    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 当前进程在后台
     *
     * @param context
     * @return true在后台, false在前台
     */
    public static boolean curProcessInBackground(Context context) {
        String processName = getCurrentActivityName(context);
        if (processName.startsWith(context.getPackageName())) {
            return false;
        }
        return true;
    }

    /**
     * 跳转VR播放器
     * @param context
     * @param scheme
     * @param vrtype
     */
    /*public static boolean startVrActivity(Context context, String scheme, String relateid, String vrtype, String sn, String usign, String channel, int type, String m3u8) {
        if(type == FocusInfo.TYPE_LIVE) {
            // 直播
            if (!TextUtils.isEmpty(scheme) && scheme.startsWith("huajiao")) {
                // 蚁视
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(scheme.trim());
                intent.setData(uri);
                context.startActivity(intent);
                return true;
            }

            if(!TextUtils.isEmpty(vrtype) && !TextUtils.isEmpty(relateid)) {
                //TODO 通用
                Intent intent = new Intent(context, VrPlayerActivity.class);
                intent.putExtra("liveid", relateid);
                intent.putExtra("type", vrtype);
                intent.putExtra("url", sn);
                intent.putExtra("usign", usign);
                intent.putExtra("channel", channel);
                context.startActivity(intent);
                return true;
            }
        }

        if(type == FocusInfo.TYPE_REPLAY) {
            // 回放VR，先跳转中间页。请求M3U8后再播放
            if(TextUtils.isEmpty(m3u8)) {
                Intent intent = new Intent(context, ActivityJumpCenter.class);
                intent.putExtra("playtid", relateid);
                context.startActivity(intent);
                return true;
            } else {
                Intent intent = new Intent(context, VrApi.class);
                intent.putExtra("streamUrl", m3u8);
                context.startActivity(intent);
                return true;
            }
        }
        return false;
    }*/

    /**
     * 跳转VR播放器
     * @param context
     * @param type
     * @param m3u8
     */
    /*public static boolean startVrReplayActivity(Context context, int type, String m3u8) {
        if(type == FocusInfo.TYPE_REPLAY) {
            Intent intent = new Intent(context, VrApi.class);
            intent.putExtra("streamUrl", m3u8);
            context.startActivity(intent);
            return true;
        }
        return false;
    }*/
    private static String sImei2;


    public static String getM2(Context context) {
        if (sImei2 != null) {
            return sImei2;
        }
        try {
            final TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            //此处调用系统函数获取ANDROID_ID，在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，这个16进制的字符串就是ANDROID_ID，当设备被wipe后该值会被重置
            String androidId = android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            //此处计算serialNo，serialNo适合操作系统相关一个参数，具体计算方法请参考下面的getDeviceSerial函数
            String serialNo = getDeviceSerial();
            //此处计算m2，下面附带有MD5Util类供参考
            sImei2 = md5Appkey("" + imei + androidId + serialNo);
            return sImei2;
        } catch (Exception e) {
            return "";
        }
    }

    private static String getDeviceSerial() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
//            LiveLog.d("jialiwei-hj", "getDeviceSerial: ");(TAG, "", e);
        }

        return serial;
    }


    public static byte[] getBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }


    public static String md5Appkey(String str) throws NoSuchAlgorithmException
    {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(getBytes(str));
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer(64);
            for (int i = 0; i < arrayOfByte.length; i++) {
                int j = 0xFF & arrayOfByte[i];
                if (j < 16)
                    localStringBuffer.append("0");
                localStringBuffer.append(Integer.toHexString(j));
            }
            return localStringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return "";
    }


    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public static void doSendSMSTo(String phoneNumber, String message
    , Context context) {
//        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("sms_body", message);
        context.startActivity(intent);
//        }
    }

    /**
     * 根据包名判断APK是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPackageExists(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
            return appInfo != null;
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    /**
     * Hashtag正则表达式
     */
    private static final Pattern hashtagPattern = Pattern.compile("#[^#]+#");

    private static String removeHashtags(String text) {
        Matcher matcher;
        String newTweet = text.trim();
        String cleanedText = "";
        while (!newTweet.equals(cleanedText)) {
            cleanedText = newTweet;
            matcher = hashtagPattern.matcher(cleanedText);
            newTweet = matcher.replaceAll("");
            newTweet = newTweet.trim();
        }
        return cleanedText;
    }

    public static List<String> getHashtags(String originalString) {
        List<String> hashtagSet = new ArrayList<String>();
        Matcher matcher = hashtagPattern.matcher(originalString);
        while (matcher.find()) {
//            int matchStart = matcher.start(1);
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String tmpHashtag = originalString.substring(matchStart, matchEnd);
            hashtagSet.add(tmpHashtag);
            originalString = originalString.replace(tmpHashtag, "");
            matcher = hashtagPattern.matcher(originalString);
        }
        return hashtagSet;
    }

    /**
     * 将某一张多媒体文件保存到了本地 刷新相册让它出现在相册中
     *
     * @param fileName 图片的本地位置
     */
    public static void insertMediaToPhotoAlbum(Context context, String fileName) {
//        try {
//            Log.d(BitmapUtils.TAG,"doNext:context:"+context+"new File(fileName):"+new File(fileName));
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    fileName, new File(fileName).getName(),
//                    new File(fileName).getName());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(fileName));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static boolean isStrAllTag(String originalString) {
        String tmp = originalString;
        Matcher matcher = hashtagPattern.matcher(tmp);
        while (matcher.find()) {
//            int matchStart = matcher.start(1);
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            String tmpHashtag = tmp.substring(matchStart, matchEnd);
            tmp = tmp.replace(tmpHashtag, "");
            matcher = hashtagPattern.matcher(tmp);
        }
        return TextUtils.isEmpty(tmp.trim());
    }

    public static List<int[]> getHashtagsIndex(String originalString) {
        List<int[]> hashtagSet = new ArrayList<int[]>();
        Matcher matcher = hashtagPattern.matcher(originalString);
        int findStart = 0;
        while (matcher.find(findStart)) {
//            int matchStart = matcher.start(1);
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            findStart = matchEnd;
            int[] array = {matchStart, matchEnd};
            hashtagSet.add(array);
//            originalString = originalString.replace(tmpHashtag, "");
            matcher = hashtagPattern.matcher(originalString);
        }
        return hashtagSet;
    }

    public static List<int[]> gettagsIndex(String originalString) {
        List<int[]> hashtagSet = new ArrayList<int[]>();
        Matcher matcher = hashtagPattern.matcher(originalString);
        int findStart = 0;
        while (matcher.find(findStart)) {
//            int matchStart = matcher.start(1);
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            findStart = matchEnd;
            int[] array = {matchStart, matchEnd};
            hashtagSet.add(array);
//            originalString = originalString.replace(tmpHashtag, "");
            matcher = hashtagPattern.matcher(originalString);
        }
        return hashtagSet;
    }
}
