package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetPlaylistsCallback;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<Playlist>> topPlaylists;
    private MutableLiveData<List<Playlist>> recentPlaylists;
    private MutableLiveData<List<Playlist>> followedPlaylists;

    @Inject
    public HomeViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.topPlaylists = new MutableLiveData<>();
        this.recentPlaylists = new MutableLiveData<>();
        this.followedPlaylists = new MutableLiveData<>();
    }

    private void requestTopPlaylists() {
        sallefyRepository.getAllPlaylistsByMostFollowed(new GetPlaylistsCallback() {
            @Override
            public void onPlaylistsReceived(List<Playlist> playlists) {
                topPlaylists.postValue(playlists);
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    private void requestRecentPlaylists() {
        sallefyRepository.getAllPlaylistsByMostRecent(new GetPlaylistsCallback() {
            @Override
            public void onPlaylistsReceived(List<Playlist> playlists) {
                recentPlaylists.postValue(playlists);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void requestFollowedPlaylists() {
        // TODO: FIX CALL
        sallefyRepository.getAllPlaylistsByMostFollowed(new GetPlaylistsCallback() {
            @Override
            public void onPlaylistsReceived(List<Playlist> playlists) {
                followedPlaylists.postValue(playlists);
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

    public LiveData<List<Playlist>> getRecentPlaylists() {
        requestRecentPlaylists();
        return recentPlaylists;
    }

    public LiveData<List<Playlist>> getFollowedPlaylists() {
        requestFollowedPlaylists();
        return followedPlaylists;
    }
}
