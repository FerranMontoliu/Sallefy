package com.example.sallefy.controller.callbacks;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public interface MusicPlayerCallback {

    void onNextTrackClicked();
    void onPreviousTrackClicked();
    void onShuffleClicked();
    void onLoopClicked();
    void onNewTrackClicked(Track track, Playlist playlist);
    void onPlayPauseClicked();

}
