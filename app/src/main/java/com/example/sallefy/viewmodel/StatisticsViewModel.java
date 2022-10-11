package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetPlaylistsCallback;
import com.example.sallefy.network.callback.GetTracksCallback;

import java.util.List;

import javax.inject.Inject;

public class StatisticsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<Playlist>> topPlaylists;
    private MutableLiveData<List<Track>> topTracks;

    @Inject
    public StatisticsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.topPlaylists = new MutableLiveData<>();
        this.topTracks = new MutableLiveData<>();
    }

    private void requestTopPlaylists() {
        sallefyRepository.getTopFollowedPlaylists(new GetPlaylistsCallback() {
            @Override
            public void onPlaylistsReceived(List<Playlist> items) {
                topPlaylists.postValue(items);
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void requestTopTracks() {
        sallefyRepository.getTopLikedTracks(new GetTracksCallback() {
            @Override
            public void onTracksReceived(List<Track> tracks) {
                topTracks.postValue(tracks);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Playlist>> getTopPlaylists() {
        requestTopPlaylists();
        return topPlaylists;
    }

    public LiveData<List<Track>> getTopTracks() {
        requestTopTracks();
        return topTracks;
    }

}
