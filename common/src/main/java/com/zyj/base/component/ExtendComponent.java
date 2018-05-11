package com.zyj.base.component;

import com.zyj.base.ui.presenter.ExtendPresenter;
import com.zyj.mvp.component.BaseComponent;
import com.zyj.mvp.rx.BaseRxPresenter;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */

public interface ExtendComponent<P extends BaseRxPresenter<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks>>
        extends BaseComponent<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks, P> {
}
