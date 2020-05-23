package com.example.sallefy.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.cloudinary.android.MediaManager;

public class CloudinaryManager {

    @SuppressLint("StaticFieldLeak")
    private static CloudinaryManager sManager;
    private Context mContext;

    public static CloudinaryManager getInstance(Context context) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context);
        }
        return sManager;
    }

    private CloudinaryManager(Context context) {
        mContext = context;
        MediaManager.init(mContext, CloudinaryConfigs.getConfigurations());
    }
}
