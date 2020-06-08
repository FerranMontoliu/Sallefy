package com.example.sallefy;

import com.example.sallefy.di.DaggerSallefyApplicationComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class SallefyApplication extends DaggerApplication {
    public static final String BASE_URL = "http://sallefy.eu-west-3.elasticbeanstalk.com/api/";

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerSallefyApplicationComponent.factory().create(getApplicationContext());
    }
}
