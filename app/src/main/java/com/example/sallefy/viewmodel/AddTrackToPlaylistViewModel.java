package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.adapter.TrackListAdapter;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.GetTracksCallback;
import com.example.sallefy.network.callback.LikeTrackCallback;
import com.example.sallefy.network.callback.UpdatePlaylistCallback;

import java.util.List;

import javax.inject.Inject;

public class AddTrackToPlaylistViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    private Playlist mPlaylist;
    private MutableLiveData<List<Track>> mTracks;

    @Inject
    public AddTrackToPlaylistViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mPlaylist = null;
        this.mTracks = new MutableLiveData<>();
    }

    public void setPlaylist(Playlist playlist) {
        this.mPlaylist = playlist;
    }

    private void requestAllTracks() {
        sallefyRepository.getAllTracks(new GetTracksCallback() {
            @Override
            public void onTracksReceived(List<Track> tracks) {
                mTracks.postValue(tracks);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public LiveData<List<Track>> getAllTracks() {
        requestAllTracks();
        return mTracks;
    }

    public void updatePlaylist(Track track, UpdatePlaylistCallback callback) {
        if (mPlaylist != null) {
            mPlaylist.addTrack(track);
        }
        sallefyRepository.updatePlaylist(mPlaylist, callback);
    }

    public void likeTrack(Track track, int position, TrackListAdapter adapter) {
        sallefyRepository.likeTrack(track, new LikeTrackCallback() {
            @Override
            public void onTrackLiked() {
                adapter.changeTrackLikeStateIcon(position);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

}
