package com.zyj.base.util;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by heyong on 16/9/19.
 */

public class ObjectUtil {

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static <T> boolean isEmpty(List<T> objectList) {
        return objectList == null || objectList.size() == 0;
    }

    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static <T> boolean equal(List<T> aList, List<T> bList) {
        return aList != null && bList != null && aList == bList && aList.size() == bList.size();
    }

    public static <T> boolean exist(T obj, List<T> list) {
        boolean exist = false;

        for(T object : list) {
            if(equal(obj, object)) {
                exist = true;
                break;
            }
        }

        return exist;
    }

}
