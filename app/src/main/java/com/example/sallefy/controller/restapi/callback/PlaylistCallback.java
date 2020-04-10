package com.example.sallefy.controller.restapi.callback;

import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;

import java.util.List;

public interface PlaylistCallback extends FailureCallback {
    void onPlaylistCreated(Playlist playlist);
    void onPlaylistFailure(Throwable throwable);
    void onPlaylistReceived(Playlist playlist);
    void onPlaylistNotReceived(Throwable throwable);
    void onPlaylistUpdated(Playlist playlist);
    void onPlaylistNotUpdated(Throwable throwable);
    void onPlaylistsReceived(List<Playlist> playlists);
    void onPlaylistsNotReceived(Throwable throwable);
    void onPlaylistFollowed();
    void onPlaylistFollowError(Throwable throwable);
    void onIsFollowedReceived(Followed followed);
}
