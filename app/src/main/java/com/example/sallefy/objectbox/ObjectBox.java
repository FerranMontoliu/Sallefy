package com.example.sallefy.objectbox;

import android.content.Context;

import com.example.sallefy.model.MyObjectBox;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        if (boxStore == null) {
            boxStore = MyObjectBox.builder()
                    .androidContext(context.getApplicationContext())
                    .build();
        }
    }

    public static BoxStore getBoxStore() {
        return boxStore;
    }
}