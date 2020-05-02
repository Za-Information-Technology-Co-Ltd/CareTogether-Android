package com.za.caretogether.service;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthInterceptor implements Interceptor {

    protected String accessToken;

    public AuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
//        builder.addHeader("app-id", "7oy7G7IstdwcanuL7tfLQFzAKwi6n57u");
//        builder.addHeader("api-key", "tFCaNkCgx9sHwXtErIqhnZS8vbfSgmnJ");
        builder.addHeader("Authorization", "Bearer "+accessToken);
        builder.addHeader("Accept", "application/json");
        builder.addHeader("X-API-TOKEN", "71b00e58e83808f05692f5b709c4c23d");
        builder.addHeader("X-APP", "MyanmarPost Manager");
        request = builder.build();

        return chain.proceed(request);
    }
}
