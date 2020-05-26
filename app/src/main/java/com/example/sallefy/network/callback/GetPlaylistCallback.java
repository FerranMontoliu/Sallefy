package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;

public interface GetPlaylistCallback extends FailureCallback {
    void onPlaylistReceived(Playlist playlist);
}
