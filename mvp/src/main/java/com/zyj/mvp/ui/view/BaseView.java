package com.zyj.mvp.ui.view;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public interface BaseView<VC> {

    void setCallbacks(VC callbacks);

    boolean isModal();
}
