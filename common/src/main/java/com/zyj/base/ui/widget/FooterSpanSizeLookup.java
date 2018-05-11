package com.zyj.base.ui.widget;

import android.support.v7.widget.GridLayoutManager;

import com.zyj.base.ui.adapter.BaseRecyclerAdapter;


/**
 * Created by Stay on 6/3/16.
 * Powered by www.stay4it.com
 */
public class FooterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private BaseRecyclerAdapter adapter;
    private int spanCount;

    public FooterSpanSizeLookup(BaseRecyclerAdapter adapter, int spanCount) {
        this.adapter = adapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.isLoadMoreFooter(position) || adapter.isSectionHeader(position)) {
            return spanCount;
        }
        return 1;
    }
}
