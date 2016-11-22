package com.wxdroid.basemodule.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;

import java.io.FileInputStream;
import java.io.InputStream;

public class LowMemoryBitmapUtils
{
    static public class BitmapWH {
        public int m_width;

        public int m_height;
    }

    public static Bitmap DecodeFile(String str_path) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(str_path);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            is = null;
        }
        if (is == null) {
            return null;
        }

        Bitmap bitmap_ret = null;
        bitmap_ret = DecodeInputStream(is);
        
        int n_degree = GlobalFunctions.getExifOrientation(str_path);

        if (n_degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(n_degree);
            try {
            	bitmap_ret = Bitmap.createBitmap(bitmap_ret, 0, 0, bitmap_ret.getWidth(), bitmap_ret.getHeight(), matrix, true);
			} catch (Throwable e) {
				// TODO: handle exception
			}
		}

        try {
            is.close();
            is = null;
        } catch (Exception e) {
            // TODO: handle exception
            is = null;
        }

        return bitmap_ret;
    }
    
    public static Bitmap DecodeFile(String str_path, int opt_scale_time, Bitmap.Config config) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(str_path);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            is = null;
        }
        if (is == null) {
            return null;
        }

        Bitmap bitmap_ret = null;
        bitmap_ret = DecodeInputStream(is, opt_scale_time, config);
        
        int n_degree = GlobalFunctions.getExifOrientation(str_path);
        
        if (n_degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(n_degree);
            try {
            	bitmap_ret = Bitmap.createBitmap(bitmap_ret, 0, 0, bitmap_ret.getWidth(), bitmap_ret.getHeight(), matrix, true);
			} catch (Throwable e) {
				// TODO: handle exception
			}
		}

        try {
            is.close();
            is = null;
        } catch (Exception e) {
            // TODO: handle exception
            is = null;
        }

        return bitmap_ret;
    }
    
    
    public static Bitmap DecodeFile(String str_path, int opt_scale_time) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(str_path);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            is = null;
        }
        if (is == null) {
            return null;
        }

        Bitmap bitmap_ret = null;
        bitmap_ret = DecodeInputStream(is, opt_scale_time);
        
        int n_degree = GlobalFunctions.getExifOrientation(str_path);
        
        if (n_degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(n_degree);
            try {
            	bitmap_ret = Bitmap.createBitmap(bitmap_ret, 0, 0, bitmap_ret.getWidth(), bitmap_ret.getHeight(), matrix, true);
			} catch (Throwable e) {
				// TODO: handle exception
			}
		}

        try {
            is.close();
            is = null;
        } catch (Exception e) {
            // TODO: handle exception
            is = null;
        }

        return bitmap_ret;
    }

    public static Bitmap DecodeResource(int n_id, Context context) {
        if (context == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(n_id);
        } catch (Throwable e) {
            is = null;
        }
        Bitmap bitmap_ret = null;
        bitmap_ret = DecodeInputStream(is);

        try {
        	if (is != null) {
        		is.close();	
			}            
            is = null;
        } catch (Exception e) {
            // TODO: handle exception
            is = null;
        }

        return bitmap_ret;
    }

    public static Bitmap DecodeInputStream(InputStream is) {
        Options opt_decord = new Options();
        opt_decord.inPurgeable = true;
        opt_decord.inInputShareable = true;
        Bitmap bitmap_ret = null;
        try {
            bitmap_ret = BitmapFactory.decodeStream(is, null, opt_decord);
        } catch (Throwable e) {
            // TODO: handle exception
            bitmap_ret = null;
        }
        return bitmap_ret;
    }
    
    public static Bitmap DecodeInputStream(InputStream is, int opt_scale_time) {
        Options opt_decord = new Options();
        opt_decord.inPurgeable = true;
        opt_decord.inInputShareable = true;
        opt_decord.inSampleSize = opt_scale_time;
        Bitmap bitmap_ret = null;
        try {
            bitmap_ret = BitmapFactory.decodeStream(is, null, opt_decord);
        } catch (Throwable e) {
            // TODO: handle exception
            bitmap_ret = null;
        }
        return bitmap_ret;
    }
    
    public static Bitmap DecodeInputStream(InputStream is, int opt_scale_time, Bitmap.Config config) {
        Options opt_decord = new Options();
        opt_decord.inPurgeable = true;
        opt_decord.inInputShareable = true;
        opt_decord.inSampleSize = opt_scale_time;
        opt_decord.inPreferredConfig = config;
        Bitmap bitmap_ret = null;
        try {
            bitmap_ret = BitmapFactory.decodeStream(is, null, opt_decord);
        } catch (Throwable e) {
            // TODO: handle exception
            bitmap_ret = null;
        }
        return bitmap_ret;
    }

    public static BitmapWH GetBitmapWH(String str_path) {
        BitmapWH b_wh_ret = new BitmapWH();
        b_wh_ret.m_height = 0;
        b_wh_ret.m_width = 0;

        FileInputStream is = null;
        try {
            is = new FileInputStream(str_path);
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            is = null;
        }
        if (is != null) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            Bitmap bm_tmp = BitmapFactory.decodeStream(is, null, options);

            try {
                is.close();
                is = null;
            } catch (Exception e) {
                // TODO: handle exception
                is = null;
            }
            b_wh_ret.m_height = options.outHeight;
            b_wh_ret.m_width = options.outWidth;
        }
        return b_wh_ret;
    }

	public static BitmapWH GetBitmapWH(Resources resource, int drawableResId) {
		BitmapWH b_wh_ret = new BitmapWH();
		b_wh_ret.m_height = 0;
		b_wh_ret.m_width = 0;

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resource, drawableResId, options);
		try {
		} catch (Exception e) {
			// TODO: handle exception
		}
		b_wh_ret.m_height = options.outHeight;
		b_wh_ret.m_width = options.outWidth;
		return b_wh_ret;
	}
}
