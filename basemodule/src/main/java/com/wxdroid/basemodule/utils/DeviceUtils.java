
package com.wxdroid.basemodule.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * 机型识别工具类.
 */
public class DeviceUtils {

    public static final String PRODUCT = Build.PRODUCT.toLowerCase();

    public static final String MODEL;

    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();

    private static final String DISPLAY = Build.DISPLAY.toLowerCase();

    private static final int ANDROID_SDK_VERSION = Build.VERSION.SDK_INT;
    public static final String ANDROID_SDK_RELEASE_VERSION = Build.VERSION.RELEASE;

    private static final String TAG = "wzt-hj";

    static { // fix model 获取不到的时候 user agent 出现非法字符导致crash
        String model = TextUtils.isEmpty(Build.MODEL.replaceAll("\\s*", "")) ? "unknown" : Build.MODEL.toLowerCase().trim();
        char c;
        boolean legal = true;
        for (int i = 0, length = model.length(); i < length; ++i) {
            c = model.charAt(i);
            if (c <= 31 || c >= 127) {
                legal = false;
                break;
            }
        }
        MODEL = legal ? model : "";
    }

    private DeviceUtils() {
    }

    public static boolean isMeizuM9() {
        return PRODUCT.contains("meizu_m9") && MODEL.contains("m9");
    }

    public static boolean isFlymeSB() {
        return DISPLAY.contains("flyme") && (PRODUCT.contains("meizu_mx") || PRODUCT.contains("m1"));
    }

    public static boolean isHtcDevice() {
        return MODEL.contains("htc") || MODEL.contains("desire");
    }

    public static boolean isLephoneDevice() {
        return PRODUCT.contains("lephone");
    }

    public static boolean isZTEU880() {
        return MANUFACTURER.equals("zte") && MODEL.contains("blade");
    }

    public static boolean  isHuawei() {
        String manufacturer = MANUFACTURER.toLowerCase();
        String brand = Build.BRAND.toLowerCase();
        if (manufacturer.contains("huawei") || brand.contains("huawei")) {
            return true;
        }
        return false;
        //return MANUFACTURER.equals("huawei") && MODEL.equals("huawei nxt-al10");
    }



    /**
     * 制造商：ZTE 型号：ZTE-U V880
     *
     * @return
     */
    public static boolean isZTEUV880() {
        return MANUFACTURER.equals("zte") && MODEL.contains("zte-u v880");
    }

    public static boolean isZTEU985() {
        return MANUFACTURER.equals("zte") && MODEL.contains("zte u985");
    }

    public static boolean isHTCHD2() {
        return MANUFACTURER.equals("htc") && MODEL.contains("hd2");
    }

    public static boolean isHTCOneX() {
        return MANUFACTURER.equals("htc") && MODEL.contains("htc one x");
    }

    public static boolean isI9100() {
        return MANUFACTURER.equals("samsung") && MODEL.equals("gt-i9100");
    }

    public static boolean isMiOne() {
        return MODEL.startsWith("mi-one");
    }

    public static boolean isGtS5830() {
        return MODEL.equalsIgnoreCase("gt-s5830");
    }

    public static boolean isGtS5830i() {
        return MODEL.equalsIgnoreCase("gt-s5830i");
    }

    public static boolean isGTP1000() {
        return MODEL.equalsIgnoreCase("gt-p1000");
    }

    public static boolean isMb525() {
        return MODEL.startsWith("mb525");
    }

    public static boolean isMe525() {
        return MODEL.startsWith("me525");
    }

    public static boolean isMb526() {
        return MODEL.startsWith("mb526");
    }

    public static boolean isMe526() {
        return MODEL.startsWith("me526");
    }

    public static boolean isMe860() {
        return MODEL.startsWith("me860");
    }

    public static boolean isMe865() {
        return MODEL.startsWith("me865");
    }

    public static boolean isXT882() {
        return MODEL.startsWith("xt882");
    }

    public static boolean isYulong() {
        return MANUFACTURER.equalsIgnoreCase("yulong");
    }

    public static boolean isKindleFire() {
        return MODEL.contains("kindle fire");
    }

    public static boolean isLGP970() {
        return MODEL.startsWith("lg-p970");
    }

    public static boolean isU8800() {
        return MODEL.startsWith("u8800");
    }

    public static boolean isU9200() {
        return MODEL.startsWith("u9200");
    }

    public static boolean isMt15i() {
        return MODEL.startsWith("mt15i");
    }

    public static boolean isDEOVOV5() {
        return MODEL.equalsIgnoreCase("deovo v5");
    }

    /**
     * 判断是否 Android 4.1
     *
     * @return
     */
    public static boolean isApiLevel16() {
        return ANDROID_SDK_VERSION >= 16;
    }

    public static boolean isIceCreamSandwich() {
        return ANDROID_SDK_VERSION >= 14;
    }

    public static boolean isHoneycomb() {
        return ANDROID_SDK_VERSION >= 11 && ANDROID_SDK_VERSION < 14;
    }

    /**
     * 判断是否大于5.0
     * @return
     */
    public static boolean isApiLevel21() {
        return ANDROID_SDK_VERSION >= 21;
    }

    /**
     * if android sdk is 2.2
     *
     * @return
     */
    public static boolean isFroyo() {
        return ANDROID_SDK_VERSION == 8;
    }

    /**
     * if android sdk is 2.1
     *
     * @return
     */
    public static boolean isEclair() {
        return ANDROID_SDK_VERSION == 7;
    }

    public static boolean isHtcG7() {
        return MANUFACTURER.equals("htc") && MODEL.equals("htc desire");
    }

    public static boolean isSendBroadcastDirectlySupported() {
        return ANDROID_SDK_VERSION < 12;
    }

    public static boolean isLockScreenSupported() {
        return !isKindleFire();
    }


    /**
     * 三星S3(I9300) MANUFACTURER: samsung, PRODUCT: m0xx ID:IMM76D,
     * brand:samsung, display:IMM76D.I9300XXBLG1, MODEL: GT-I9300, screen:
     * 720*1280 DPI:320
     *
     * @return
     */
    public static boolean isS3() {
        return MANUFACTURER.equals("samsung") && MODEL.equals("gt-i9300");
    }

    public static boolean isXiaomi() {
        return MANUFACTURER.contains("xiaomi");
    }

    public static boolean isMiuiRom() {
        return DISPLAY.contains("miui") || DISPLAY.indexOf("mione") >= 0;
    }

    public static boolean isMiOneRom() {
        // Log.d("DeviceUtils", "DISPLAY = " + DISPLAY + "--- " + MODEL);
        return DISPLAY.indexOf("mione") >= 0;
    }

    public static boolean isMiTowS() {
        // Log.d("DeviceUtils", "DISPLAY = " + DISPLAY + "--- " + MODEL);
        return "mi 2s".equals(MODEL);
    }

    public static boolean isOppoX909() {
        // Log.d("DeviceUtils", "DISPLAY = " + DISPLAY + "--- " + MODEL);
        return "x909".equals(MODEL);
    }

    /**
     * 特工机 deovo V5 MANUFACTURER: NVIDIA, PRODUCT: kai ID:IML74K, brand:generic,
     * display:IML74K.V03.023.1992-user, MODEL: deovo V5, screen: 720*1184
     * DPI:320
     *
     * @return
     */
    public static boolean isDeovoV5() {
        return MODEL.contains("deovo v5");
    }

    /**
     * 特工机 BOVO MANUFACTURER: BOVO, PRODUCT: full_blaze ID:IMM76D, brand:BOVO,
     * display:IMM76D, MODEL: S-F16, screen: 720*1184 DPI:320
     *
     * @return
     */
    public static boolean isBOVO() {
        return MANUFACTURER.equals("bovo") && MODEL.equals("s-f16");
    }

    /**
     * 某些机型的 PackageManager.resolveActivity 以及
     * PackageManager.queryIntentActivities 返回为空 此处对这些机型做特殊处理
     *
     * @param action
     * @param uri
     * @return
     */
    public static ComponentName resolveActivity(String action, String uri) {
        if (isZTEUV880()) {
            if ("android.intent.action.VIEW".equals(action) && "content://com.android.contacts/contacts".equals(uri)) {
                ComponentName cn = new ComponentName("com.android.contacts", "com.android.contacts.DialtactsContactsEntryActivity");
                return cn;
            }
        }
        return null;
    }

    public static boolean isGalaxyNexusAndApiLevel16() {
        return MODEL.equalsIgnoreCase("Galaxy Nexus") && ANDROID_SDK_VERSION == 16;
    }

    /**
     * 4.2
     *
     * @return
     */
    public static boolean isAfterApiLevel17() {
        return ANDROID_SDK_VERSION >= 17;
    }

    /**
     * 夏新大V
     *
     * @return
     */
    public static boolean isBigV() {
        return ("amoi n826".equalsIgnoreCase(MODEL) || "amoi n821".equalsIgnoreCase(MODEL) || "amoi n820".equalsIgnoreCase(MODEL));
    }


    /**
     * chenlimiao
     */
    /**
     * @Title: dip2px
     * @Description: TODO(根据手机的分辨率从 dp 的单位 转成为 px(像素))
     * @param: @param context
     * @param: @param dpValue
     * @param: @return 设定文件
     * @return: int 返回类型
     * @date: 2013-8-19 下午2:38:08
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @Title: px2dip
     * @Description: TODO(根据手机的分辨率从 px(像素) 的单位 转成为 dp )
     * @param: @param context
     * @param: @param pxValue
     * @param: @return 设定文件
     * @return: int 返回类型
     * @date: 2013-8-19 下午2:38:24
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            Log.d(TAG, "CPU Count: " + files.length);
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return 1;
        }
    }

    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 实时获取CPU当前频率（单位KHZ）
    public static String getCurCpuFreq() {
        String result = "N/A";
        try {
            FileReader fr = new FileReader(
                    "/sys/devices/system/cpu/cpu1/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // 获取CPU名字
    public static String getCpuName() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获得可用的内存
    public static long getmem_UNUSED(Context context) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间

        MEM_UNUSED = mi.availMem / 1024;
        return MEM_UNUSED;
    }

    // 获得总内存
    public static long getmem_TOLAL() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }

    private static final String cpuStat = "/proc/stat";
    /**
     * 返回 CPU 的占用率。
     *
     * @return CPU 占用率如果是 35%，返回整数 35。
     */
    public static double getCpuUsage() {
        // 实际上是从 /proc/stat 里面读取的
        try {

            CPUTime startTime = new CPUTime();
            CPUTime endTime = new CPUTime();

            getcpuTime(startTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getcpuTime(endTime);

            double cpuUsage = 0;
            long totalTime = endTime.getTotalTime() - startTime.getTotalTime();
            if (totalTime == 0) {
                cpuUsage = 0;
            } else {
                cpuUsage = 1 - (((double) (endTime.getIdleTime() - startTime.getIdleTime())) / totalTime);
            }
            System.out.println("the cpu usage is: " + cpuUsage * 100 + "%");
            return cpuUsage;

        } catch (Throwable th) {
        }
        return 0;
    }

    private static void getcpuTime(CPUTime t) {
        BufferedReader fr = null;
        try {
            fr = new BufferedReader(new FileReader(new File(cpuStat)));

            String oneLine = null;
            while ((oneLine = fr.readLine()) != null) {
                if (oneLine.startsWith("cpu ")) {
                    String[] vals = oneLine.substring(4).split(" ");
                    if (vals.length < 10) {
                        System.err.println("read an error line string!");
                    } else {
                        t.setTotalTime(Long.parseLong(vals[1]) + Long.parseLong(vals[2]) + Long.parseLong(vals[3])
                                + Long.parseLong(vals[4]) + Long.parseLong(vals[5]) + Long.parseLong(vals[6])
                                + Long.parseLong(vals[7]) + Long.parseLong(vals[8]) + Long.parseLong(vals[9]));
                        t.setIdleTime(Long.parseLong(vals[4]));
                        break;
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class CPUTime {
        private long totalTime;
        private long idleTime;

        public CPUTime() {
            totalTime = 0;
            idleTime = 0;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }

        public long getIdleTime() {
            return idleTime;
        }

        public void setIdleTime(long idleTime) {
            this.idleTime = idleTime;
        }

        @Override
        public String toString() {
            return "CPUTime{" +
                    "totalTime=" + totalTime +
                    ", idleTime=" + idleTime +
                    '}';
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int x = 0;
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

}
