package com.zyj.mvp.ui.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.zyj.mvp.ui.display.BaseDisplay;
import com.zyj.mvp.ui.view.BaseView;
import com.zyj.mvp.util.Preconditions;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public abstract class BasePresenter<V extends BaseView<VC>, VC> {
    private BaseDisplay mDisplay;
    private boolean mInited;
    private final Set<V> mViews;
    private final Set<V> mUnModifiableViews;

    public BasePresenter() {
        mViews = new CopyOnWriteArraySet<>();
        mUnModifiableViews = Collections.unmodifiableSet(mViews);
    }

    @CallSuper
    public void init() {
        Preconditions.checkState(!mInited, "Already inited");
        mInited = true;
        onInited();
    }

    public void resume() {

    }

    public void pause() {

    }

    @CallSuper
    public void suspend() {
        Preconditions.checkState(mInited, "Not inited");
        onSuspended();
        mInited = false;
    }

    public final boolean isInited() {
        return mInited;
    }

    @CallSuper
    public void attachView(V view) {
        Preconditions.checkArgument(view != null, "view cannot be null");
        Preconditions.checkState(!mViews.contains(view), "view is already attached");

        mViews.add(view);

        view.setCallbacks(createUiCallbacks(view));

        if (isInited()) {
            onViewAttached(view);
            populateView(view);
        }
    }

    @CallSuper
    public void detachView(V view) {
        Preconditions.checkArgument(view != null, "view cannot be null");
        Preconditions.checkState(!mViews.contains(view), "view is already attached");

        onViewDetached(view);

        view.setCallbacks(null);

        mViews.remove(view);
    }

    protected synchronized final void populateViews() {
        for (V view : mViews) {
            populateView(view);
        }
    }

    protected int getId(V view) {
        return view.hashCode();
    }

    protected synchronized V findView(final int id) {
        for (V view : mViews) {
            if (getId(view) == id) {
                return view;
            }
        }
        return null;
    }

    protected void onInited() {
        if (!mViews.isEmpty()) {
            for (V view : mViews) {
                onViewAttached(view);
                populateView(view);
            }
        }
    }

    protected void onSuspended() {}

    protected void onViewAttached(V view) {}

    protected void populateView(V view) {}

    protected void onViewDetached(V view) {}

    protected final void assertInited() {
        Preconditions.checkState(mInited, "Must be init'ed to perform action");
    }

    protected abstract VC createUiCallbacks(V view);

    protected final Set<V> getViews() {
        return mUnModifiableViews;
    }

    @NonNull
    public BaseDisplay getDisplay() {
        return mDisplay;
    }

    public void setDisplay(BaseDisplay mDisplay) {
        this.mDisplay = mDisplay;
    }

}
