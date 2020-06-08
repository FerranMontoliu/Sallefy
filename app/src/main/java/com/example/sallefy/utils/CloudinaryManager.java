package com.example.sallefy.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cloudinary.android.MediaManager;

public class CloudinaryManager {

    @SuppressLint("StaticFieldLeak")
    private static CloudinaryManager sManager;

    public static CloudinaryManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context);
        }
        return sManager;
    }

    private CloudinaryManager(Context context) {
        MediaManager.init(context, CloudinaryConfigs.getConfigurations());
    }
}
