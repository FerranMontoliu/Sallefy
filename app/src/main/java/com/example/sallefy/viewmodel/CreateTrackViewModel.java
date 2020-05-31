package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Genre;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GenreCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateTrackViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<String>> genres;

    @Inject
    public CreateTrackViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.genres = new MutableLiveData<>();
    }

    private void requestGenres(){
        sallefyRepository.getAllGenres(new GenreCallback() {
            @Override
            public void onGenresReceived(List<Genre> results) {
                ArrayList<String> spinnerItems = new ArrayList<>();
                for (Genre g : results) {
                    spinnerItems.add(g.getName());
                }
                genres.postValue(spinnerItems);
            }

            @Override
            public void onNoGenres(Throwable throwable) {}

            @Override
            public void onFailure(Throwable throwable) {}

        });
    }

    public LiveData<List<String>> getAllGenres(){
        requestGenres();
        return genres;
    }

    public void addTrack(){

    }
}
