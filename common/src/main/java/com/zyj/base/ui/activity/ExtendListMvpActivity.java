package com.zyj.base.ui.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.zyj.base.R;
import com.zyj.base.ui.adapter.BaseRecyclerAdapter;
import com.zyj.base.ui.adapter.BaseViewHolder;
import com.zyj.base.ui.presenter.ExtendPresenter;
import com.zyj.base.ui.widget.PullRecycler;
import com.zyj.base.ui.widget.layoutmanager.ILayoutManager;
import com.zyj.base.ui.widget.layoutmanager.MyLinearLayoutManager;
import com.zyj.base.util.ObjectUtil;
import com.zyj.mvp.component.BaseComponent;
import com.zyj.mvp.rx.BaseRxPresenter;

import java.util.List;

/**
 * Created by heyong on 2016/11/28.
 */

public abstract class ExtendListMvpActivity<T, P extends BaseRxPresenter<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks>,
        C extends BaseComponent<ExtendPresenter.ExtendView, ExtendPresenter.ExtendCallbacks, P>>
        extends ExtendMvpActivity<P, C> implements PullRecycler.OnRecyclerRefreshListener {
    
    protected PullRecycler recycler;
    protected BaseRecyclerAdapter mAdapter;
    protected List<T> mDataList;

    @Override
    protected void bindView() {
        super.bindView();
        recycler = (PullRecycler) findViewById(R.id.mPullRecycler);
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.enableLoadMore(true);
    }

    @Override
    protected void bindData() {
        mAdapter = new RecyclerAdapter();
        recycler.setAdapter(mAdapter);
    }

    protected void setDataItems(List<T> items) {
        if (!ObjectUtil.isEmpty(items)) {
            mDataList = items;
            mAdapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.ac_ui_extend_list;
    }
    
    @Override
    protected boolean autoBindViews() {
        return false;
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
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
            return this.isSectionHeader(position);
        }


    }

}
