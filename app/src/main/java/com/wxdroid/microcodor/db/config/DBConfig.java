package com.wxdroid.microcodor.db.config;

import android.text.TextUtils;

import com.wxdroid.microcodor.util.StringUtil;


/**
 * Created by jinchun on 16/11/16.
 */

public class DBConfig {
    private static final String DATABASE_NAME = "microcodor"; // 保存的数据库文件名
    private static final String DATABASE_TEMP_NAME = "microcodor_temp"; // 保存的数据库文件名
    private static final int DATABASE_VERSION = 15;// 数据库版本号
    private static final String DATABASE_NAME_SUFFIX = ".db";// 数据库后缀

    /**
     * 获取数据库名
     * @param appendName
     * @return
     */
    public static String getDatabaseName(String appendName){
        if(TextUtils.isEmpty(appendName)){
            return DATABASE_NAME + DATABASE_NAME_SUFFIX;
        }else{
            appendName = StringUtil.fiterEmptyCharacter(appendName);
            appendName = StringUtil.fiterSpecialCharacter(appendName);
            if(TextUtils.isEmpty(appendName)){
                return DATABASE_NAME + DATABASE_NAME_SUFFIX;
            }else{
                return DATABASE_NAME + "_" + appendName + DATABASE_NAME_SUFFIX;
            }
        }
    }

    /**
     * 获取临时数据库名
     * @return
     */
    public static String getDatabaseTempName(){
        return DATABASE_TEMP_NAME + DATABASE_NAME_SUFFIX;
    }

    /**
     * 获取数据库版本号
     * @return
     */
    public static int getDatabaseVersion(){
        return DATABASE_VERSION;
    }
}
