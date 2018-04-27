package com.zyj.mvp.ui.presenter;

import com.zyj.mvp.ui.view.BaseView;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public interface GetPresenterDelegate<V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>> {

    BasePresenterDelegate<V, VC, P> getPresenterDelegate();
}
