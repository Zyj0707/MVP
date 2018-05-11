package com.zyj.base.ui.activity;

import android.os.Bundle;

import com.zyj.base.ui.fragment.TransactionCommitter;
import com.zyj.base.ui.presenter.ExtendPresenter;
import com.zyj.mvp.component.BaseComponent;
import com.zyj.mvp.rx.BaseRxPresenter;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */

public abstract class ExtendMvpActivity<P extends BaseRxPresenter<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks>,
        C extends BaseComponent<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks, P>>
        extends BaseMvpActivity<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks, P, C>
        implements ExtendPresenter.ExtendCallbacks, TransactionCommitter {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // inject argument first
        super.onCreate(savedInstanceState);
        if (mPresenter instanceof ExtendPresenter) {
            ((ExtendPresenter)mPresenter).setHostCallbacks(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter instanceof ExtendPresenter) {
            ((ExtendPresenter)mPresenter).setHostCallbacks(null);
        }
    }
}
