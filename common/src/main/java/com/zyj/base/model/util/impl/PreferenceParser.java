package com.zyj.base.model.util.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.zyj.base.model.util.PreferenceDelegate;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */

public class PreferenceParser implements PreferenceDelegate {
    private static final String PREFERENCE_NAME = "gank";      //首选项名
    private final Context mContext;

    public PreferenceParser(Context context) {
        mContext = context;
    }

    @Override
    public String getStringValue(String name) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);

        return pref.getString(name, null);
    }

    @Override
    public int getIntValue(String name) {
        SharedPreferences pref = mContext.getSharedPreferences(PREFERENCE_NAME, 0);

        return pref.getInt(name, 0);
    }
}
