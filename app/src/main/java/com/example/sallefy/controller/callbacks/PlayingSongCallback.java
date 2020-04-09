package com.example.sallefy.controller.callbacks;

public interface PlayingSongCallback {

    void onErrorPreparingMediaPlayer();
    void onTrackDurationReceived(int duration);
}
