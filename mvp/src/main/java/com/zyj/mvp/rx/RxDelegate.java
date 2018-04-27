package com.zyj.mvp.rx;

import com.zyj.mvp.util.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Zyj on 2018/4/27.
 * Email: zyj0707@outlook.com
 */

public class RxDelegate {
    private CompositeDisposable mDisposables2Stop;
    private CompositeDisposable mDisposables2Destroy;

    public synchronized boolean addUtilStop(Disposable disposable) {
        Preconditions.checkState(mDisposables2Stop != null, "addUtilStop should be called between onStart and onStop");
        mDisposables2Stop.add(disposable);
        return true;
    }

    public synchronized boolean addUtilDestroy(Disposable disposable) {
        Preconditions.checkState(mDisposables2Destroy != null, "addUtilDestroy should be called between onCrate and onDestroy");
        mDisposables2Destroy.add(disposable);
        return true;
    }

    public synchronized void remove(Disposable disposable) {
        Preconditions.checkState(mDisposables2Stop != null && mDisposables2Destroy != null, "remove should not be called after onDestroy");
        if (mDisposables2Stop != null) {
            mDisposables2Stop.remove(disposable);
        }

        if (mDisposables2Destroy != null) {
            mDisposables2Destroy.remove(disposable);
        }
    }

    public synchronized void onCreate() {
        Preconditions.checkState(mDisposables2Destroy == null, "onCreate called multiple times");
        mDisposables2Destroy = new CompositeDisposable();
    }

    public synchronized void onStart() {
        Preconditions.checkState(mDisposables2Stop == null, "onStart called multiple times");
        mDisposables2Stop = new CompositeDisposable();
    }

    public synchronized void onStop() {
        Preconditions.checkState(mDisposables2Stop != null, "onStop called multiple times or onStart not called");
        mDisposables2Stop.dispose();
        mDisposables2Stop = null;
    }

    public synchronized void onDestroy() {
        Preconditions.checkState(mDisposables2Destroy != null, "onDestroy called multiple times or onCreate not called");
        mDisposables2Destroy.dispose();
        mDisposables2Destroy = null;
    }


}
