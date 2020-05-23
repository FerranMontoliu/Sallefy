package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Genre;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GenreCallback;

import java.util.List;

import javax.inject.Inject;

public class CreateTrackViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    public static final int PICK_IMAGE = 0;
    public static final int PICK_FILE = 1;

    private MutableLiveData<List<Genre>> mGenres;

    @Inject
    public CreateTrackViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        mGenres = new MutableLiveData<>();
    }

    private void requestGenres() {
        sallefyRepository.getAllGenres(new GenreCallback() {
            @Override
            public void onGenresReceived(List<Genre> genres) {
                mGenres.postValue(genres);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Genre>> getGenres() {
        requestGenres();
        return mGenres;
    }
}
