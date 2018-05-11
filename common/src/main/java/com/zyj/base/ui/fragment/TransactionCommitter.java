package com.zyj.base.ui.fragment;


/**
 * {@link android.support.v4.app.FragmentTransaction} from, we should commit only when it's resumed,
 * avoiding the Activity state loss error.
 */
public interface TransactionCommitter {
    /**
     * whether the host is resumed
     *
     * @return {@code true} if it's resumed.
     */
    boolean isCommitterResumed();
}
