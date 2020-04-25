package com.example.sallefy.di;

import android.content.Context;

import com.example.sallefy.SallefyApplication;
import com.example.sallefy.di.module.ActivityModule;
import com.example.sallefy.di.module.FragmentModule;
import com.example.sallefy.di.module.NetworkModule;
import com.example.sallefy.di.module.SallefyModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        SallefyModule.class,
        NetworkModule.class,
        FragmentModule.class,
        ActivityModule.class,
})
public interface SallefyApplicationComponent extends AndroidInjector<SallefyApplication> {

    @Component.Factory
    interface Factory {
        SallefyApplicationComponent create(@BindsInstance Context context);
    }
}
