package com.example.sallefy.utils;

import android.media.AudioAttributes;
import android.media.MediaPlayer;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public class CustomMediaPlayer extends MediaPlayer {
    private boolean prepared;
    private boolean preparing;
    private boolean waiting;
    private Track track;
    private Playlist playlist;
    private int currentPlaylistTrack;

    //trackIndex -1 if queue
    public CustomMediaPlayer(Track track, Playlist playlist, int trackIndex) {
        this.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

        this.prepared = false;
        this.waiting = false;
        this.track = track;
        this.playlist = playlist;

        this.setOnCompletionListener(mp -> {
            if (prepared) {
                if (MusicPlayer.getInstance().isLoop()) {
                    MusicPlayer.getInstance().restart();
                } else {
                    MusicPlayer.getInstance().onNextTrackClicked();
                }
            }
        });
        currentPlaylistTrack = trackIndex;
    }

    public boolean isPreparing() {
        return preparing;
    }

    public void setPreparing(boolean mPreparing) {
        this.preparing = mPreparing;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void setPrepared(boolean mPrepared) {
        this.prepared = mPrepared;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean mWaiting) {
        this.waiting = mWaiting;
    }

    public Track getTrack() {
        return track;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public int getCurrentPlaylistTrack() {
        return currentPlaylistTrack;
    }

    public void setCurrentPlaylistTrack(int currentPlaylistTrack) {
        this.currentPlaylistTrack = currentPlaylistTrack;
    }
}
