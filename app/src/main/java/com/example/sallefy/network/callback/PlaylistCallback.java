package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;


public interface PlaylistCallback extends FailureCallback {
    void onPlaylistReceived(Playlist playlist);

    void onPlaylistNotReceived(Throwable throwable);

    void onPlaylistUpdated(Playlist playlist);

    void onPlaylistNotUpdated(Throwable throwable);
}
