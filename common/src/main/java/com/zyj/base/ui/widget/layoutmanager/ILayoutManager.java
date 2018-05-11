package com.zyj.base.ui.widget.layoutmanager;

import android.support.v7.widget.RecyclerView;

import com.zyj.base.ui.adapter.BaseRecyclerAdapter;


public interface ILayoutManager {

    RecyclerView.LayoutManager getLayoutManager();

    int findLastVisiblePosition();

    void setUpAdapter(BaseRecyclerAdapter adapter);

}
