package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.LikeTrackCallback;

import javax.inject.Inject;

public class PlayingSongViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;
    private Playlist playlist;
    private MutableLiveData<Boolean> mIsLiked;

    @Inject
    public PlayingSongViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        mIsLiked = new MutableLiveData<>();
    }

    public LiveData<Boolean> isLiked() {
        requestIsLiked();
        return mIsLiked;
    }

    private void requestIsLiked() {
        mIsLiked.postValue(track.isLiked());
    }

    public void likeTrackToggle() {
        sallefyRepository.likeTrack(track, new LikeTrackCallback() {
            @Override
            public void onTrackLiked() {
                track.setLiked(Boolean.FALSE.equals(mIsLiked.getValue()));
                mIsLiked.postValue(track.isLiked());
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
