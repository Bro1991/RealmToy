package com.memolease.realmtoy.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bro on 2017-08-17.
 */

public class NApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header("X-Naver-Client-Id", "teVvjcuUv2l1YHemrsvn")
                .header("X-Naver-Client-Secret", "HQHyKUrvIK")
                .method(original.method(), original.body())
                .build();

        return chain.proceed(request);
    }
}
