package com.example.sallefy.viewmodel;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Search;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetPlaylistsCallback;
import com.example.sallefy.network.callback.SearchCallback;

import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    private MutableLiveData<Search> searchResults;

    private EditText search;

    @Inject
    public SearchViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;

        this.searchResults = new MutableLiveData<>();
    }

    private void requestSearchResults() {
        sallefyRepository.getSearchResults(search.getText().toString(), new SearchCallback() {
            @Override
            public void onSearchResultsReceived(Search results) {
                searchResults.postValue(results);
            }

            @Override
            public void onNoResults(Throwable throwable) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }

        });
    }

    public LiveData<Search> getSearch() {
        requestSearchResults();
        return searchResults;
    }

}
