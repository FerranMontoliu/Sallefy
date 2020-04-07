package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.Search;

import java.util.List;

public interface SearchCallback extends FailureCallback{
    void onSearchResultsReceived(Search results);
    void onNoResults(Throwable throwable);

}
