package com.zyj.base.ui.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.zyj.base.R;
import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.view.BaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 2016/11/6.
 */

public abstract class BaseTabMvpFragment<V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>>
        extends BaseMvpFragment<V, VC, P> {
    private static final String SAVE_SELECTED_TAB = "selected_tab";

    protected ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter mAdapter;
    private int mCurrentItem = 0;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current", mCurrentItem);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentItem = savedInstanceState != null ? savedInstanceState.getInt("current") : 0;
    }

    @CallSuper
    @Override
    protected void bindData() {
        mAdapter = new TabPagerAdapter(getChildFragmentManager());
    }

    @CallSuper
    @Override
    protected void bindView(View rootView) {
        super.bindView(rootView);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        mViewPager.addOnPageChangeListener(onPageChangeListener);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    protected void setFragments(List<Fragment> fragments) {
        mAdapter.setFragments(fragments);

        if (mCurrentItem >= mAdapter.getCount())
            mCurrentItem = 0;
        mViewPager.setCurrentItem(mCurrentItem);
    }

    protected abstract String getTabTitle(int position);

    protected TabPagerAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ac_ui_base_tab;
    }

    @Override
    protected boolean autoBindViews() {
        return false;
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentItem = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class TabPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragments;

        private TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<>();
        }

        void setFragments(List<Fragment> fragments) {
            mFragments.clear();
            mFragments.addAll(fragments);
            notifyDataSetChanged();
        }

        @Override
        public final Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public final int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTabTitle(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
