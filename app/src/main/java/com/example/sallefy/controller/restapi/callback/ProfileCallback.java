package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.User;

import java.util.List;

public interface ProfileCallback extends FailureCallback {
    void onUserInfoReceived(User userData);
    void onFollowToggle();
    void onFollowFailure(Throwable throwable);
    void onIsFollowedReceived(Boolean isFollowed);
    void onTracksReceived(List<Track> tracks);
    void onNoTracks(Throwable throwable);
    void onPlaylistsReceived(List<Playlist> playlists);
    void onPlaylistsNotReceived(Throwable throwable);
}
