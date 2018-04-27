package com.zyj.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zyj.mvp.component.BaseComponent;
import com.zyj.mvp.ui.display.BaseDisplay;
import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.presenter.BasePresenterDelegate;
import com.zyj.mvp.ui.presenter.GetPresenterDelegate;
import com.zyj.mvp.ui.view.BaseView;
import com.zyj.mvp.util.Preconditions;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public abstract class MvpDiActivity <V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>,
        C extends BaseComponent<V, VC, P>> extends AppCompatActivity
        implements GetPresenterDelegate<V, VC, P> {

    protected BaseDisplay display;
    protected C component;
    protected P mPresenter;
    private BasePresenterDelegate<V, VC, P> mPresenterDelegate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeDependence();
        initializDisplay();
        Preconditions.checkNotNull(component, "component not inited");
        mPresenter = component.presenter();
        mPresenterDelegate = new BasePresenterDelegate<V, VC, P>() {
            @Override
            protected P createPresenter() {
                return MvpDiActivity.this.createPresenter();
            }
        };
        mPresenterDelegate.onCreate(display);

    }

    @Override
    protected void onStart() {
        mPresenterDelegate.onStart();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mPresenterDelegate.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mPresenterDelegate.onDestory();
        mPresenterDelegate = null;
        display = null;
        super.onDestroy();
    }

    protected abstract void initializeDependence();

    protected void initializDisplay() {}

    public BasePresenterDelegate<V, VC, P> getmPresenterDelegate() {
        return mPresenterDelegate;
    }

    protected final P createPresenter() {
        return mPresenter;
    }
}
