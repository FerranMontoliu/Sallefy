package com.example.sallefy.network.callback;

import java.util.List;

public interface GetItemsCallback extends FailureCallback {
    void onItemsReceived(List<Object> items);
}
