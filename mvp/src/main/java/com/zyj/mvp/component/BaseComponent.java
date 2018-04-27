package com.zyj.mvp.component;

import android.support.annotation.NonNull;

import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.view.BaseView;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public interface BaseComponent<V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>> {

    @NonNull
    P presenter();
}
