
package com.wxdroid.basemodule.network.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONUtils<T> {
    public static Gson gson = new Gson();

    public static <T> T fromJson(Class<T> type, String source) throws IllegalAccessException, InstantiationException
    {
        try {
            T result = gson.fromJson(source, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type.newInstance();
    }

    public static <T> T fromJson(Class<T> type, JsonElement source) {
        try {
            T result = gson.fromJson(source.toString(), type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析静态字符串
     * @param type
     * @param source
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonArray(Class<T[]> type, String source) {
        try {
            T[] list = gson.fromJson(source, type);
            //备注：不能直接返回Arrays.asList(list)，此处需要包一层ArrayList转换。
            // 因为Arrays.asList(list)返回的是Arrays$ArrayList 非ArrayList对象，
            // 但两者都继承自AbstractList，而Arrays$ArrayList并没有实现add、remove、clear方法，
            // 后续使用的话会抛throw UnsupportedOperationException
            return new ArrayList<T>(Arrays.asList(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<T>();
    }

    public static <T> String toJson(T obj) {
        String result = gson.toJson(obj);
        return result;
    }
}
