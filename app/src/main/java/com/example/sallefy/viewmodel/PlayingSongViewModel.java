package com.example.sallefy.viewmodel;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.LikeTrackCallback;
import com.example.sallefy.utils.MusicPlayer;

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

    public LiveData<Boolean> isLiked(){
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
                track.setLiked(!mIsLiked.getValue());
                mIsLiked.postValue(track.isLiked());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void setTrack(Track track) {
        this.track = track;
        checkIfVideo();
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

    private boolean checkIfVideo(){
        String[] urlSplit = track.getUrl().split("\\.");
        String extension = urlSplit[urlSplit.length-1];

        if (extension.equals("mp4")) {
            track.setHasVideo(true);
            return true;
        }
        track.setHasVideo(false);
        return false;
    }
}
