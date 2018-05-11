package com.zyj.base.ui.interfaces;


import com.zyj.base.ui.fragment.BaseMvpFragment;
import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.view.BaseView;

/**
 * Created by lan on 2017-10-23.
 */

public interface BackHandledInterface<V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>> {

    void setSelectedFragment(BaseMvpFragment<V, VC, P> backHandledFragment);

}
