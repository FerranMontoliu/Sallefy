package com.example.sallefy.controller.restapi.callback;

import java.util.List;

public interface SearchCallback extends FailureCallback{
    void onSearchResultsReceived(List<Object> results);
    void onNoResults(Throwable throwable);

}
