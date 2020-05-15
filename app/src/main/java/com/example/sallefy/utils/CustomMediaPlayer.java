package com.example.sallefy.utils;

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
    private int mCurrentPlaylistTrack;

    //trackIndex -1 if queue
    public CustomMediaPlayer(Track track, Playlist playlist, int trackIndex){
        this.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMusicPlayer = new MediaPlayer();
        mPrepared = false;
        mWaiting = false;
        mTrack = track;
        mPlaylist = playlist;

        this.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (MusicPlayer.getInstance().isLoop()) {
                    MusicPlayer.getInstance().restart();

                } else {
                    MusicPlayer.getInstance().onNextTrackClicked();
                }
            }
        });
        mCurrentPlaylistTrack = trackIndex;
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

    public int getmCurrentPlaylistTrack() {
        return mCurrentPlaylistTrack;
    }

    public void setmCurrentPlaylistTrack(int mCurrentPlaylistTrack) {
        this.mCurrentPlaylistTrack = mCurrentPlaylistTrack;
    }
}
