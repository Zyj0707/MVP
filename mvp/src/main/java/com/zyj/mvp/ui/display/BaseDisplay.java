package com.zyj.mvp.ui.display;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public interface BaseDisplay {

    void finish();

    void showUpNavigation(boolean show);

    void setActionBarTitle(String title);

    void setSupportActionBar(Object toolbar);

}
