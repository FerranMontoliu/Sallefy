package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetPlaylistsCallback;
import com.example.sallefy.network.callback.UpdatePlaylistCallback;

import java.util.List;

import javax.inject.Inject;

public class AddSongToPlaylistViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    private Track mTrack;
    private MutableLiveData<List<Playlist>> mPlaylists;

    @Inject
    public AddSongToPlaylistViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mTrack = null;
        this.mPlaylists = new MutableLiveData<>();
    }

    public void setTrack(Track track) {
        this.mTrack = track;
    }

    public void updatePlaylist(Playlist playlist, UpdatePlaylistCallback callback) {
        if (mTrack != null) {
            playlist.addTrack(mTrack);
        }
        sallefyRepository.updatePlaylist(playlist, callback);
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
