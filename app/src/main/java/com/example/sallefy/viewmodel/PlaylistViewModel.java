package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.auth.Session;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.FollowCheckCallback;
import com.example.sallefy.network.callback.FollowToggleCallback;

import javax.inject.Inject;

public class PlaylistViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Playlist playlist;
    private MutableLiveData<Boolean> mIsFollowed;

    @Inject
    public PlaylistViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.playlist = null;
        this.mIsFollowed = new MutableLiveData<>();
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public String getPlaylistThumbnail(){
        return playlist.getThumbnail();
    }

    public String getPlaylistName(){
        return playlist.getName();
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public boolean isOwnUser(){
        if (this.playlist.getUser() != null){
            return this.playlist.getUser().equals(Session.getUser());
        }
        return false;
    }

    private void requestIsFollowed(){
        sallefyRepository.isPlaylistFollowed(getPlaylist(), new FollowCheckCallback() {
            @Override
            public void onObjectFollowedReceived(boolean isFollowed) {
                mIsFollowed.postValue(isFollowed);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mIsFollowed.postValue(false);
            }
        });
    }

    public LiveData<Boolean> isFollowed(){
        requestIsFollowed();
        return mIsFollowed;
    }

    public void followPlaylistToggle(){
        sallefyRepository.followPlaylistToggle(getPlaylist(), new FollowToggleCallback() {
            @Override
            public void onObjectFollowChanged() {
                mIsFollowed.postValue(!mIsFollowed.getValue());
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }
}
