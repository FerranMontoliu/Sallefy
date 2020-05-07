package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetTracksCallback;

import java.util.List;

import javax.inject.Inject;

public class ProfileTracksViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<Track>> mTracks;

    @Inject
    public ProfileTracksViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mTracks = new MutableLiveData<>();
    }

    private void requestUserTracks(String username) {
        sallefyRepository.getUserTracks(username, new GetTracksCallback() {
            @Override
            public void onTracksReceived(List<Track> tracks) {
                mTracks.postValue(tracks);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Track>> getUserTracks(String username) {
        requestUserTracks(username);
        return mTracks;
    }
}
