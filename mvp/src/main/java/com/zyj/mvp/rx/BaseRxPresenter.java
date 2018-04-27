package com.zyj.mvp.rx;

import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.view.BaseView;

import io.reactivex.disposables.Disposable;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public abstract class BaseRxPresenter<V extends BaseView<VC>, VC> extends BasePresenter<V, VC> {
    private final RxDelegate mRxDelegate;

    protected BaseRxPresenter() {
        mRxDelegate = new RxDelegate();
    }

    protected boolean addUtilStop(Disposable disposable) {
        return mRxDelegate.addUtilStop(disposable);
    }

    public boolean addUtilDestroy(Disposable disposable) {
        return mRxDelegate.addUtilDestroy(disposable);
    }

    public void remove(Disposable disposable) {
        mRxDelegate.remove(disposable);
    }

    @Override
    public void init() {
        mRxDelegate.onCreate();
        super.init();
    }

    @Override
    public void resume() {
        super.resume();
        mRxDelegate.onStart();
    }

    @Override
    public void pause() {
        super.pause();
        mRxDelegate.onStop();
    }

    @Override
    public void suspend() {
        mRxDelegate.onDestroy();
        super.suspend();
    }
}
