package com.example.sallefy.network.callback;

import com.example.sallefy.model.Search;

public interface SearchCallback extends FailureCallback {
    void onSearchResultsReceived(Search results);

    void onNoResults(Throwable throwable);

}
