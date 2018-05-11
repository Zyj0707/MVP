package com.zyj.base.state;


import com.zyj.base.util.ObjectUtil;
import com.zyj.mvp.util.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heyong on 2016/11/14.
 */

public interface BaseState {

    void registerForEvent(Object receiver);

    void unregisterForEvent(Object receiver);

    class UiCausedEvent {
        public final int callingId;

        public UiCausedEvent(int callingId) {
            this.callingId = callingId;
        }
    }

    class BaseArgumentEvent<T> {
        public final T item;

        public BaseArgumentEvent( T item) {
            this.item = Preconditions.checkNotNull(item, "item cannot be null");
        }
    }

    abstract class PagedResult<T> {
        public List<T> items;
        public int page;
        public int pageSize;  //每页多少数

        public PagedResult(int pageSize) {
            items = new ArrayList<>();
            page = -1;
            this.pageSize = pageSize;
        }

        public boolean full() {
            return page * pageSize == ((items != null) ? items.size() : 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PagedResult that = (PagedResult) o;
            return ObjectUtil.equal(items, that.items);
        }

        @Override
        public int hashCode() {
            return items != null ? items.hashCode() : 0;
        }
    }
}
