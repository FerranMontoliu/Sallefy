package com.example.sallefy.controller;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.sallefy.controller.callbacks.MusicPlayerCallback;
import com.example.sallefy.controller.callbacks.PlayingSongCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayer implements MusicPlayerCallback {

    private static MusicPlayer musicPlayer = null;

    private static final String PLAY_VIEW = "paused";
    private static final String PAUSE_VIEW = "playing";
    private static final int PREVIOUS_TRACK_BUFFER_SIZE = 10;

    private static PlayingSongCallback mPlayingSongCallback;

    private int currentPlaylistTrack;
    private ArrayList<Track> mQueue;
    private Track[] previousTracksbuffer;

    //If queue is empty, next track will be from the playlist
    private Playlist mPlaylist;

    //Double Buffering
    private MediaPlayer mPrimaryPlayer;
    private boolean mPrimaryPrepared;
    private boolean mWaitingPrimary;
    private MediaPlayer mBackPlayer;
    private boolean mBackPrepared;

    private boolean shuffle;
    private boolean loop;
    private String state;

    private MusicPlayer(PlayingSongCallback playingSongCallback){
        mPlayingSongCallback = playingSongCallback;

        previousTracksbuffer = new Track[PREVIOUS_TRACK_BUFFER_SIZE];
        state = PLAY_VIEW;
        mPrimaryPlayer = new MediaPlayer();
        mPrimaryPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mBackPlayer = new MediaPlayer();
        mBackPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPrimaryPrepared = false;
        mWaitingPrimary = false;
        mBackPrepared = false;

        mPrimaryPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                getmPlayingSongCallback().onTrackDurationReceived(mPrimaryPlayer.getDuration());
                mPrimaryPrepared = true;
                if (mWaitingPrimary) {
                    onPlayPauseClicked();
                    mWaitingPrimary = false;
                }
            }
        });

    }

    public static MusicPlayer getInstance(PlayingSongCallback playingSongCallback){
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer(playingSongCallback);
        } else {
            mPlayingSongCallback = playingSongCallback;
        }

        return musicPlayer;
    }

    @Override
    public void onNextTrackClicked() {

    }

    @Override
    public void onPreviousTrackClicked() {

    }

    @Override
    public void onShuffleClicked() {

    }

    @Override
    public void onLoopClicked() {

    }

    @Override
    public void onNewTrackClicked(Track track, Playlist playlist) {
        state = PLAY_VIEW;
        mPrimaryPrepared = false;
        preparePrimaryPlayer(track.getUrl());

        if (playlist != null && playlist.getTracks() != null) {
            currentPlaylistTrack = findIndexTrackInPlaylist(track, playlist);

            if (currentPlaylistTrack != -1) {
                mPlaylist = playlist;
            } else{
                mPlaylist = null;
            }

        }

        this.onPlayPauseClicked();
    }

    @Override
    public void onPlayPauseClicked() {

        if (state.equals(PLAY_VIEW)) {

            if(mPrimaryPrepared) {
                mPrimaryPlayer.start();
                state = PAUSE_VIEW;
                mPlayingSongCallback.onPlayTrack();
            } else {
                mWaitingPrimary = true;
            }

        } else {
            mPrimaryPlayer.pause();
            state = PLAY_VIEW;
            mPlayingSongCallback.onPauseTrack();
        }

    }

    private int findIndexTrackInPlaylist(Track targetTrack, Playlist playlist) {
        int i = 0;

        for (Track track: playlist.getTracks()) {
            if (track.getName().equals(targetTrack.getName())){
                return i;
            }
            i++;
        }

        return -1;
    }

    private void preparePrimaryPlayer(final String url) {
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPrimaryPlayer.reset();
                    mPrimaryPlayer.setDataSource(url);
                    mPrimaryPlayer.prepare();
                } catch (IOException e) {
                    mPlayingSongCallback.onErrorPreparingMediaPlayer();
                }
            }
        });
        connection.start();
    }


    public PlayingSongCallback getmPlayingSongCallback() {
        return mPlayingSongCallback;
    }
}
