package com.zyj.base.model.config;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;


/**
 * Created by lan on 2016/11/30.
 */

public class RetryInterceptor implements Interceptor {
    private final int RETRY;

    public RetryInterceptor(final int retryTimeout) {
        RETRY = retryTimeout;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        int tryCount = 0;

        do {
            try {
                // try the request
                response = chain.proceed(request);
            } catch (IOException e) {
                e.printStackTrace();
                Timber.e("Request is not successful - %d", tryCount);
            }
            tryCount++;
        }while ((response == null || !response.isSuccessful()) && tryCount < RETRY);

        if ((response == null || !response.isSuccessful()) && tryCount == RETRY) {
            throw new IOException("连接主机失败");
        }

        return response;
    }

}
