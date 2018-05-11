package com.zyj.base.model.util.impl;

import android.content.Context;

import com.zyj.base.model.util.ResDelegate;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */

public class ResParser implements ResDelegate {

    private final Context mContext;

    public ResParser(Context context) {
        mContext = context;
    }

    @Override
    public int getDrawableId(String imgName) {
        return mContext.getResources().getIdentifier(imgName, "drawable", mContext.getPackageName());
    }

    @Override
    public String getStringRes(int strId) {
        return mContext.getString(strId);
    }

    @Override
    public int getColorRes(int colorId) {
        return mContext.getResources().getColor(colorId);
    }

    @Override
    public float getDimensionRes(int dimenId) {
        return mContext.getResources().getDimension(dimenId);
    }
}
