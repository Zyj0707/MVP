package com.zyj.base.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zyj.base.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */

public abstract class BaseActivity extends SafelyAppCompatActivity {
    private Unbinder mUnBinder;
    private Toolbar mToolbar;
    private TextView mTitle;

    private static final TypedValue sTypedValue = new TypedValue();
    private int mColorPrimaryDark;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // inject argument first
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        bindData();
        bindView();
        //  initSystemBarTint();
    }

    @TargetApi(19)
    private void initSystemBarTint(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager = null;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);

            getTheme().resolveAttribute(R.attr.colorPrimaryDark, sTypedValue, true);
            mColorPrimaryDark = sTypedValue.data;

            tintManager.setStatusBarTintColor(ContextCompat.getColor(this, mColorPrimaryDark));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    protected void setDisplayBack() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setTitle(String text) {
        if(mTitle != null) {
            mTitle.setText(text);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindView();
    }

    protected boolean autoBindViews() {
        return true;
    }

    @CallSuper
    protected void bindView() {
        if (autoBindViews()) {
            mUnBinder = ButterKnife.bind(this);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.tvTitle);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected void unbindView() {
        if (autoBindViews() && mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    protected void bindData() {}

    protected abstract int getLayoutRes();

    public final void finishTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.finishAfterTransition();
        }
        else {
            this.finish();
        }
    }

    private Bundle provideOptionsBundle(Pair<View, String>[] pairs) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs).toBundle();
    }
}
