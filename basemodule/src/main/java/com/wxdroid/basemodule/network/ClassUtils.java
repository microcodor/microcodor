package com.wxdroid.basemodule.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ClassUtils
{

    /**
     * 获取子类subClassT实现父类classT的泛型
     *
     * @param subClassT
     *            支持泛型的子类
     * @param classT
     *            支持泛型的父类
     * @return 泛型
     */
    public static Class<?> getTClass(Class<?> subClassT, Class<?> classT) {
        if (subClassT == null || classT == null) {
            return null;
        }

        TypeVariable<?>[] tvs = classT.getTypeParameters();
        if (tvs.length != 1 || !classT.isAssignableFrom(subClassT) || classT.isPrimitive() || classT.isArray()
                || classT.isEnum() || classT.isAnnotation()) {
            return null;
        }

        if (classT.isInterface()) {
            return getTClassFromInterface(subClassT, classT);
        } else {
            return getTClassFromClass(subClassT, classT);
        }
    }

    private static Class<?> getTClassFromInterface(Class<?> subClassT, Class<?> classT) {
        boolean find = false;
        while (subClassT != null && classT.isAssignableFrom(subClassT) && !find) {
            Class<?>[] interfaces = subClassT.getInterfaces();
            for (int i = 0; i < interfaces.length && !find; i++) {
                if (classT.isAssignableFrom(interfaces[i])) {
                    find = true;
                    break;
                }
            }
            if (find) {
                break;
            }
            subClassT = subClassT.getSuperclass();
        }

        if (find) {
            return getTClassFromInterface0(subClassT, classT);
        } else {
            return null;
        }
    }

    private static Class<?> getTClassFromInterface0(Class<?> subClassT, Class<?> classT) {
        Class<?>[] interfaces = subClassT.getInterfaces();
        Type[] types = subClassT.getGenericInterfaces();
        for (int i = 0; i < interfaces.length && i < types.length; i++) {
            if (classT == interfaces[i]) {
                if (types[i] instanceof ParameterizedType) {
                    // 获取此类型实际类型参数的Type对象数组，数组里放的都是对应类型的Class
                    Type[] generics = ((ParameterizedType) types[i]).getActualTypeArguments();
                    if (generics == null || generics.length < 1 || !(generics[0] instanceof Class)) {
                        return null;
                    }
                    return (Class<?>) generics[0];
                }
            } else if (classT.isAssignableFrom(interfaces[i])) {
                return getTClassFromInterface0(interfaces[i], classT);
            }
        }
        return null;
    }

    private static Class<?> getTClassFromClass(Class<?> subClassT, Class<?> classT) {
        Class<?> superClass = subClassT.getSuperclass();
        boolean find = false;
        while (classT.isAssignableFrom(superClass) && !find) {
            if (classT == superClass) {
                find = true;
                break;
            }
            subClassT = superClass;
            superClass = subClassT.getSuperclass();
        }

        if (find) {
            Type superType = subClassT.getGenericSuperclass();// 得到泛型父类
            // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
            if (!(superType instanceof ParameterizedType)) {
                return null;
            }
            // 获取此类型实际类型参数的Type对象数组，数组里放的都是对应类型的Class
            Type[] generics = ((ParameterizedType) superType).getActualTypeArguments();
            if (generics == null || generics.length < 1 || !(generics[0] instanceof Class)) {
                return null;
            }
            return (Class<?>) generics[0];
        } else {
            return null;
        }
    }
}
