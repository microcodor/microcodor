package com.wxdroid.microcodor.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * Created by jinchun on 2016/12/11.
 */

public class BitmapUtil {
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
