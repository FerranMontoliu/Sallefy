package com.example.sallefy.network;

import com.example.sallefy.auth.TokenManager;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class TokenInterceptor implements Interceptor {
    private TokenManager tokenManager;

    @Inject
    public TokenInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = tokenManager.getToken();
        if (TokenManager.isTokenValid(token)) {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            return chain.proceed(request);
        }

        return chain.proceed(chain.request());
    }
}
