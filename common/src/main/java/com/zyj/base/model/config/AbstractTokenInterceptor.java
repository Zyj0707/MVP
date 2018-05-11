package com.zyj.base.model.config;


import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by lan on 2017-10-30.
 */

public abstract class AbstractTokenInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    protected static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mClient;

    public AbstractTokenInterceptor(OkHttpClient mClient) {
        this.mClient = mClient;
        this.mClient.interceptors().add(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // try the request
        Response originalResponse = chain.proceed(request);

        if (isTokenExpired(originalResponse)){//根据和服务端的约定判断token过期
            //同步请求方式，获取最新的Token
            boolean newSession = loadNewToken();

            if (newSession) {
                //使用新的Token，创建新的请求
                Request newRequest = chain.request()
                        .newBuilder()
                        .build();
                //重新请求
                return chain.proceed(newRequest);
            }
        }

        // otherwise just pass the original response on
        return originalResponse;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
    protected abstract boolean isTokenExpired(Response response) throws IOException ;

    /**
     * 同步请求方式，获取最新的Token
     * @return
     */
    protected abstract boolean loadNewToken() throws IOException ;

    protected String responseBodyStr(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }
        return buffer.clone().readString(charset);
    }

}
