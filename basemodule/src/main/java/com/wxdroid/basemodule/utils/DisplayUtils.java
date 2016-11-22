package com.wxdroid.basemodule.utils;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DisplayUtils
{

    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;

    /**
     * 屏幕真正高度，去掉底部触摸屏
     */
    public static int realScreenHeight;

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getWidth(Context context) {
        if (screenWidth == 0) {
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getHeight(Context context) {
        if (screenHeight == 0) {
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        }
        return screenHeight;
    }

    /**
     * 获得标题栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> mClass = null;
        Object obj = null;
        Field field = null;
        int resID = 0, height = 0;
        try {
            mClass = Class.forName("com.android.internal.R$dimen");
            obj = mClass.newInstance();
            field = mClass.getField("status_bar_height");
            resID = Integer.parseInt(field.get(obj).toString());
            height = context.getResources().getDimensionPixelSize(resID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    /**
     * 获取屏幕真正高度，去掉底部触摸屏
     * @return
     */
    public static int getRealHeight(Context context) {

        if(realScreenHeight == 0) {

            WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display d = w.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            d.getMetrics(metrics);
            // since SDK_INT = 1;
            realScreenHeight = metrics.heightPixels;
            // includes window decorations (statusbar bar/navigation bar)
            if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
                try {
                    realScreenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
                } catch (Exception ignored) {
                }
                // includes window decorations (statusbar bar/navigation bar)
            } else if (Build.VERSION.SDK_INT >= 17) {
                try {
                    android.graphics.Point realSize = new android.graphics.Point();
                    Display.class.getMethod("getRealSize", android.graphics.Point.class).invoke(d, realSize);
                    realScreenHeight = realSize.y;
                } catch (Exception ignored) {
                }
            }
        }
        return realScreenHeight;
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean mmerseModel = false;

    /**
     * 设置通知栏沉浸模式
     * @param window
     */
    public static void setStatusBarModel(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resultMiui = DisplayUtils.setStatusBarTextColor(window, 1);
            int resultFl = DisplayUtils.setStatusBarDarkIcon(window, true);
            //透明状态栏
            if (resultMiui == 1 || resultFl == 1) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mmerseModel = true;
            }
        }
    }

    /* 只支持MIUI V6
    * @param context
    * @param type 0--只需要状态栏透明 1-状态栏透明且黑色字体 2-清除黑色字体
    */
    public static int setStatusBarTextColor(Window window, int type) {
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (type == 0) {
                extraFlagField.invoke(window, tranceFlag, tranceFlag);//只需要状态栏透明
            } else if (type == 1) {
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);//状态栏透明且黑色字体
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 2;
        }
        return 1;
    }


    /**
     * 是否是魅族手机
     *
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static int setStatusBarDarkIcon(Window window, boolean dark) {
        if (!isFlyme()) {
            return 0;
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
                return 2;
            }
        }
        return 1;
    }

    /**
     * 判断横竖屏
     * @param context
     * @return
     */
    public static boolean isLandScreen(Context context) {
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    private static float rotation;
    /**
     * 获取床空rotate
     * @param context
     * @return
     */
    public static float getRotation(Context context) {
        if(rotation == 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
            rotation = wm.getDefaultDisplay().getRotation();
        }
        return rotation;
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

}
