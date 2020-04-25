package com.example.sallefy.callback;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public interface MusicPlayerCallback {

    void onNextTrackClicked();

    void onPreviousTrackClicked();

    void onShuffleClicked();

    void onLoopClicked();

    void onNewTrackClicked(Track track, Playlist playlist);

    void onPlayPauseClicked();

    void onProgressChanged(int progress);

    void onSetNextTrack(Track track, Playlist playlist);

}
