package com.zyj.base.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.FrameLayout;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import timber.log.Timber;

public class ProgressWebView extends WebView {
    private static final String TAG = ProgressWebView.class.getSimpleName();
    private Context context;
    private SmoothProgressBar progressbar;
    private OnWebInteractive onInteractive;

    private long startTime = 0;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView(context);
        initSetting(context);
        setWebViewClient(new WebViewClient());
        setWebChromeClient(new WebChromeClient());
    }

    private void initView(Context context) {
        progressbar = new SmoothProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setIndeterminate(true);

        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.TOP;
        progressbar.setLayoutParams(params2);

        addView(progressbar);
    }

    private void initSetting(Context context) {
        WebSettings settings = getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        String dbPath = context.getApplicationContext().getDir( "database" , Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dbPath);

        settings.setAppCacheMaxSize(1024*1024*5);
        String  appCacheDir  =  context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCacheDir);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);

        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //先不要自动加载图片，等页面finish后再发起图片加载
        if(Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
    }

    public void setOnInteractive(OnWebInteractive onInteractive) {
        this.onInteractive = onInteractive;
    }

    @Override
    public void destroy() {
        stopLoading();
        removeAllViews();
        context = null;
        super.destroy();
    }

    public class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            super.shouldOverrideUrlLoading(view, url);
            Timber.tag(TAG).d("shouldOverrideUrlLoading url:" + url);

            loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            startTime = System.currentTimeMillis();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Timber.tag(TAG).d("onReceivedError url:" + request.getUrl());
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            long currentTimeMillis = System.currentTimeMillis();
            Timber.tag(TAG).d("url:" + url + "|TIME:" + (currentTimeMillis-startTime));
            startTime = 0;
            if(!getSettings().getLoadsImagesAutomatically()) {
                getSettings().setLoadsImagesAutomatically(true);
            }

            Timber.tag(TAG).d("onPageFinished url:" + url);
        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);

            if(onInteractive != null) {
                onInteractive.onReceiveTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);

                if(startTime != 0) {
                    long currentTimeMillis = System.currentTimeMillis();
                    double time = (currentTimeMillis - startTime) / 1000.0;
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota, quotaUpdater);
            Timber.tag(TAG).e("onExceededDatabaseQuota url:" + url);
            quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
        }

        @Override
        public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
            quotaUpdater.updateQuota(2*requiredStorage);
        }
    }

    public interface OnWebInteractive {
        void onReceiveTitle(String title);
    }

}
