package com.zyj.base.model.config;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Zyj on 2018/4/28.
 * Email: zyj0707@outlook.com
 */

public class CookieManager implements CookieJar{
    private final PersistentCookieStore persistentCookieStore;

    public CookieManager(Context context) {
        persistentCookieStore = new PersistentCookieStore(context);
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        persistentCookieStore.add(url, cookies);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = persistentCookieStore.get(url);

        return cookies != null ? cookies : new ArrayList<Cookie>();
    }

    public void clear() {
        persistentCookieStore.removeAll();
    }
}
