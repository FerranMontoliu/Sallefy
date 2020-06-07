package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;

import java.util.List;

public interface GetItemsCallback extends FailureCallback {
    void onItemsReceived(List<Object> items);
}
