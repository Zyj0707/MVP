package com.zyj.base.ui.fragment;

import android.support.annotation.CallSuper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zyj.base.R;
import com.zyj.base.ui.adapter.BaseRecyclerAdapter;
import com.zyj.base.ui.adapter.BaseViewHolder;
import com.zyj.base.ui.widget.PullRecycler;
import com.zyj.base.ui.widget.layoutmanager.ILayoutManager;
import com.zyj.base.ui.widget.layoutmanager.MyLinearLayoutManager;
import com.zyj.base.util.ObjectUtil;
import com.zyj.base.util.StringUtil;
import com.zyj.mvp.ui.presenter.BasePresenter;
import com.zyj.mvp.ui.view.BaseView;

import java.util.List;

/**
 * Created by heyong on 2016/11/5.
 */

public abstract class BaseListMvpFragment<T, V extends BaseView<VC>, VC, P extends BasePresenter<V, VC>>
        extends BaseMvpFragment<V, VC, P> implements PullRecycler.OnRecyclerRefreshListener {
    protected static final int PAGE_SIZE = 8;
    protected static final int START = 1;
    private LinearLayout layContent, layLoading, layLoadFail, layEmpty;
    protected PullRecycler recycler;
    private TextView tvLoadFailed;
    protected BaseRecyclerAdapter mAdapter;
    protected List<T> mDataList;
    private boolean init = true;         //用于视图加载

    @Override
    @CallSuper
    protected void bindData() {
        mAdapter = new RecyclerAdapter();
    }

    @Override
    @CallSuper
    protected void bindView(View rootView) {
        super.bindView(rootView);
        layContent = (LinearLayout) rootView.findViewById(R.id.layoutContent);
        layLoading = (LinearLayout) rootView.findViewById(R.id.layoutLoading);
        layLoadFail = (LinearLayout) rootView.findViewById(R.id.layoutLoadFailed);
        layEmpty = (LinearLayout) rootView.findViewById(R.id.layoutEmpty);
        tvLoadFailed = (TextView) rootView.findViewById(R.id.tvLoadFailed);
        recycler = (PullRecycler) rootView.findViewById(R.id.mPullRecycler);
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.setAdapter(mAdapter);
        recycler.enableLoadMore(true);
        layInit();
    }

    private void layInit() {
        if (layEmpty != null) {
            View reloadView = layEmpty.findViewById(R.id.tvReload);
            if (reloadView != null)
                setViewOnClick(reloadView);
        }

        if (layLoadFail != null) {
            View reloadView = layLoadFail.findViewById(R.id.tvReload);
            if (reloadView != null)
                setViewOnClick(reloadView);
        }

        setViewVisiable(layContent, View.VISIBLE);
        setViewVisiable(layEmpty, View.GONE);
        setViewVisiable(layLoadFail, View.GONE);
        setViewVisiable(layLoading, View.GONE);
    }

    private void setViewOnClick(View v) {
        if (v == null)
            return;

        v.setOnClickListener(innerOnClickListener);
    }

    private View.OnClickListener innerOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            /**
             * 视图点击回调，子类重写
             */
            setLoading();
            onRefreshData(true);
        }
    };

    protected void setLoading() {
        setViewVisiable(layLoading, View.VISIBLE);
        setViewVisiable(layLoadFail, View.GONE);
        setViewVisiable(layEmpty, View.GONE);
        setViewVisiable(layContent, View.GONE);
    }

    protected void setViewVisiable(View v, int visibility) {
        if (v != null)
            v.setVisibility(visibility);
    }

    protected void setDataItems(List<T> items) {
        boolean enableLoad = false;
        if (!ObjectUtil.isEmpty(items)) {
            mDataList = items;
            enableLoad = (items.size() == PAGE_SIZE);
            mAdapter.notifyDataSetChanged();
        }
        recycler.enableLoadMore(enableLoad);
        recycler.onRefreshCompleted();

        if(init) {
            if(ObjectUtil.isEmpty(items)) {
                setViewVisiable(layLoading, View.GONE);
                setViewVisiable(layContent, View.GONE);
                setViewVisiable(layLoadFail, View.GONE);
                setViewVisiable(layEmpty, View.VISIBLE);
            }
            else {
                init = false;

                setViewVisiable(layLoading, View.GONE);
                setViewVisiable(layContent, View.VISIBLE);
                setViewVisiable(layLoadFail, View.GONE);
                setViewVisiable(layEmpty, View.GONE);
            }
        }
    }

    protected void loadFail(String errorMsg) {         //加载数据失败时
        recycler.onRefreshCompleted();

        if(init) {
            setViewVisiable(layLoading, View.GONE);
            setViewVisiable(layContent, View.GONE);
            setViewVisiable(layEmpty, View.GONE);
            setViewVisiable(layLoadFail, View.VISIBLE);

            if (!StringUtil.isEmpty(errorMsg)) {
                tvLoadFailed.setText(errorMsg);
            }
            else {
                tvLoadFailed.setText(R.string.load_fail);
            }
        }
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            onRefreshData(true);
        }
        else if (action == PullRecycler.ACTION_LOAD_MORE_REFRESH) {
            onRefreshData(false);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ac_ui_base_list;
    }

    @Override
    protected boolean autoBindViews() {
        return false;
    }

    protected void onRefreshData(boolean refresh) {}

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);

    public class RecyclerAdapter extends BaseRecyclerAdapter {

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListMvpFragment.this.isSectionHeader(position);
        }

    }

}
