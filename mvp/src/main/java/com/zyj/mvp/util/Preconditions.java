package com.zyj.mvp.util;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public class Preconditions {

    public static <T> T checkNotNull(T object, String errorMsg) {
        if (object == null) {
            throw new NullPointerException(errorMsg);
        }
        return object;
    }

    public static void checkState(boolean state, String errorMsg) {
        if (!state) {
            throw new IllegalStateException(errorMsg);
        }
    }

    public static void checkArgument(boolean state, String errorMsg) {
        if (!state) {
            throw new IllegalStateException(errorMsg);
        }
    }


}
