package com.example.sallefy.controller;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public class CustomMediaPlayer extends MediaPlayer{

    private MediaPlayer mMusicPlayer;
    private boolean mPrepared;
    private boolean mWaiting;
    private Track mTrack;
    private Playlist mPlaylist;

    public CustomMediaPlayer(Track track, Playlist playlist){
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMusicPlayer = new MediaPlayer();
        mPrepared = false;
        mWaiting = false;
        mTrack = track;
        mPlaylist = playlist;

        this.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                MusicPlayer.getInstance().onNextTrackClicked();
            }
        });
    }

    public boolean isPrepared() {
        return mPrepared;
    }

    public void setPrepared(boolean mPrepared) {
        this.mPrepared = mPrepared;
    }

    public boolean isWaiting() {
        return mWaiting;
    }

    public void setWaiting(boolean mWaiting) {
        this.mWaiting = mWaiting;
    }

    public Track getTrack() {
        return mTrack;
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }
}