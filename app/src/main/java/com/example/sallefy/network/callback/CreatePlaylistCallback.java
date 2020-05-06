package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;

public interface CreatePlaylistCallback extends FailureCallback {
    void onPlaylistCreated(Playlist playlist);
}
