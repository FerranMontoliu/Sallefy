package com.example.sallefy.utils;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import javax.inject.Singleton;

@Singleton
public class NavigationFixer {

    public static void adjustGravity(View v) {
        if (v.getId() == com.google.android.material.R.id.smallLabel) {
            ViewGroup parent = (ViewGroup) v.getParent();
            parent.setPadding(0, 0, 0, 0);

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) parent.getLayoutParams();
            params.gravity = Gravity.CENTER;
            parent.setLayoutParams(params);
        }

        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;

            for (int i = 0; i < vg.getChildCount(); i++) {
                adjustGravity(vg.getChildAt(i));
            }
        }
    }

    public static void adjustWidth(BottomNavigationView nav) {
        try {
            Field menuViewField = nav.getClass().getDeclaredField("mMenuView");
            menuViewField.setAccessible(true);
            Object menuView = menuViewField.get(nav);

            assert menuView != null;
            Field itemWidth = menuView.getClass().getDeclaredField("mActiveItemMaxWidth");
            itemWidth.setAccessible(true);
            itemWidth.setInt(menuView, Integer.MAX_VALUE);
        }
        catch (Exception ignored) {

        }
    }

}
