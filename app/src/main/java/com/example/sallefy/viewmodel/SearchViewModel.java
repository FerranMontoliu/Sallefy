package com.example.sallefy.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Search;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.SearchCallback;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    private MutableLiveData<Search> searchResults;

    @Inject
    public SearchViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.searchResults = new MutableLiveData<>();
    }

    private void requestSearchResults(String searchText) {
        sallefyRepository.getSearchResults(searchText, new SearchCallback() {
            @Override
            public void onSearchResultsReceived(Search results) {
                searchResults.postValue(results);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<Search> getSearch(String searchText) {
        requestSearchResults(searchText);
        return searchResults;
    }
}
