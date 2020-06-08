package com.example.sallefy.callback;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public interface PlayingSongCallback {

    void onErrorPreparingMediaPlayer();

    void onTrackDurationReceived(int duration);

    void onPlayTrack();

    void onPauseTrack();

    void onChangedTrack(Track track, Playlist playlist);
}
