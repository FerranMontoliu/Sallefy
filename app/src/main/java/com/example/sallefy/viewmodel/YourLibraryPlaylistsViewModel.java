package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.CreatePlaylistCallback;
import com.example.sallefy.network.callback.GetPlaylistsCallback;

import java.util.List;

import javax.inject.Inject;

public class YourLibraryPlaylistsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private MutableLiveData<List<Playlist>> mPlaylists;

    @Inject
    public YourLibraryPlaylistsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mPlaylists = new MutableLiveData<>();
    }

    private void requestOwnPlaylists() {
        sallefyRepository.getOwnPlaylists(new GetPlaylistsCallback() {
            @Override
            public void onPlaylistsReceived(List<Playlist> playlists) {
                mPlaylists.postValue(playlists);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Playlist>> getOwnPlaylists() {
        requestOwnPlaylists();
        return mPlaylists;
    }
}
