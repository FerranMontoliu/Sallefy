package com.example.sallefy.utils;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

public class MarginItemDecorator extends RecyclerView.ItemDecoration {
    private int spacePx;

    @Inject
    public MarginItemDecorator(DisplayMetrics displayMetrics) {
        this.spacePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == RecyclerView.HORIZONTAL) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = spacePx;
                }
                outRect.top = spacePx;
            } else {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = spacePx;
                }
                outRect.left = spacePx;
            }

            outRect.right = spacePx;
            outRect.bottom = spacePx;
        }
    }
}
