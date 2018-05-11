package com.zyj.base.ui.presenter;

import com.zyj.mvp.rx.BaseRxPresenter;
import com.zyj.mvp.ui.view.BaseView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */

public abstract class ExtendPresenter extends BaseRxPresenter<ExtendPresenter.ExtendView,
        ExtendPresenter.ExtendCallbacks> {

    private ExtendCallbacks mCallbacks;

    @Override
    protected ExtendCallbacks createUiCallbacks(ExtendView view) {
        return new ExtendCallbacks() {
            @Override
            public void finish() {
                if(mCallbacks != null) {
                    mCallbacks.finish();
                }
            }
        };
    }

    public void setHostCallbacks(ExtendCallbacks extendCallbacks) {
        mCallbacks = extendCallbacks;
    }

    public interface ExtendCallbacks {
        void finish();
    }

    public interface ExtendView extends BaseView<ExtendCallbacks> {}

    public interface ExtendListView<T extends Serializable> extends ExtendView {
        void setItems(List<T> items);
    }

    public interface StringListView extends ExtendListView<String> {

    }
}
