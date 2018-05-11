package com.zyj.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zyj.base.R;
import com.zyj.base.ui.activity.BaseMvpActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lan on 2016/10/27.
 */

public abstract class BaseFragment extends SafelySupportFragment {
    private Unbinder mUnBinder;
    private Toolbar mToolbar;
    private TextView mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(shouldHaveOptionsMenu());
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mTitle = (TextView) view.findViewById(R.id.tvTitle);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        bindView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbindView();
    }

    protected abstract int getLayoutRes();

    protected boolean shouldHaveOptionsMenu() {
        return false;
    }

    protected boolean autoBindViews() {
        return true;
    }

    @CallSuper
    protected void bindView(final View rootView) {
        if (autoBindViews()) {
            mUnBinder = ButterKnife.bind(this, rootView);
        }
    }

    protected void bindData(){}

    protected void unbindView() {
        if (autoBindViews() && mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    protected final void setSupportActionBar(Toolbar toolbar) {
        ((BaseMvpActivity) getActivity()).setSupportActionBar(toolbar);
    }

}
