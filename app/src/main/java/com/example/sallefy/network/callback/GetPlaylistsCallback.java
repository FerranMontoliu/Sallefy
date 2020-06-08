package com.example.sallefy.network.callback;

import com.example.sallefy.model.Playlist;

import java.util.List;

public interface GetPlaylistsCallback extends FailureCallback {
    void onPlaylistsReceived(List<Playlist> playlists);
}
