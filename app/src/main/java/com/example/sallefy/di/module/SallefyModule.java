package com.example.sallefy.di.module;

import android.content.Context;
import android.util.DisplayMetrics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SallefyModule {

    @Provides
    @Singleton
    public static DisplayMetrics provideDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
