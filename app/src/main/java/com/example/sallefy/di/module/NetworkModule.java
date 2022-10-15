package com.example.sallefy.di.module;

import com.example.sallefy.BuildConfig;
import com.example.sallefy.network.SallefyService;
import com.example.sallefy.network.TokenInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    public static OkHttpClient provideOkHttpClient(TokenInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    public static SallefyService provideSallefyService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(SallefyService.class);
    }
}
