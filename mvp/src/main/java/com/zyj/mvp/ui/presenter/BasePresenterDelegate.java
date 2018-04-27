package com.zyj.mvp.ui.presenter;

import com.zyj.mvp.ui.display.BaseDisplay;
import com.zyj.mvp.ui.view.BaseView;
import com.zyj.mvp.util.Preconditions;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public abstract class BasePresenterDelegate<V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>> {
    private P mPresenter;

    public void onCreate(BaseDisplay display) {
        mPresenter = createPresenter();
        checkPresenter();
        mPresenter.setDisplay(display);
        mPresenter.init();
    }

    public void onStart() {
        checkPresenter();
        mPresenter.resume();
    }

    public void onStop() {
        checkPresenter();
        mPresenter.pause();
    }

    public void attachView(V view) {
        checkPresenter();
        mPresenter.attachView(view);
    }

    public void detachView(V view) {
        checkPresenter();
        mPresenter.detachView(view);
    }

    public void onDestory() {
        checkPresenter();
        mPresenter.suspend();
        mPresenter.setDisplay(null);
    }

    protected abstract P createPresenter();

    private void checkPresenter() {
        Preconditions.checkState(mPresenter != null, "You must call BasePresenterDelegate #onCreate!" +
        "And createPresenter must return non-null");
    }
}
