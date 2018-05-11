package com.zyj.base.model.util;

/**
 * Created by Zyj on 2018/5/2.
 * Email: zyj0707@outlook.com
 */

public interface ResDelegate {

    int getDrawableId(String imgName);

    String getStringRes(int strId);

    int getColorRes(int colorId);

    float getDimensionRes(int dimenId);
}
