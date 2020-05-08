package com.example.sallefy.adapter.callback;

public interface LikeableListAdapter extends IListAdapter {
    void onItemLiked(Object item, int position);

    void onItemMore(Object item);
}
