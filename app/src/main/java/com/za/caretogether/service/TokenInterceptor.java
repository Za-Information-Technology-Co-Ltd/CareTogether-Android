package com.za.caretogether.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        builder.addHeader("app-id", "7oy7G7IstdwcanuL7tfLQFzAKwi6n57u");
        builder.addHeader("api-key", "tFCaNkCgx9sHwXtErIqhnZS8vbfSgmnJ");
        request = builder.build();

        return chain.proceed(request);
    }
}