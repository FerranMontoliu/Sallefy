package com.example.sallefy.controller;

import android.media.MediaPlayer;

import com.example.sallefy.controller.callbacks.MusicPlayerCallback;
import com.example.sallefy.controller.callbacks.PlayingSongCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

public class MusicPlayer implements MusicPlayerCallback {

    private static MusicPlayer musicPlayer = null;

    private static final String PLAY_VIEW = "paused";
    private static final String PAUSE_VIEW = "playing";
    private static final int PREVIOUS_TRACKS_BUFFER_SIZE = 10;

    private static PlayingSongCallback mPlayingSongCallback;

    private int mCurrentPlaylistTrack;
    private Queue<Track> mQueue;
    private Deque<CustomMediaPlayer> mPreviousPlayers;

    //If queue is empty, next track will be from the playlist
    private Playlist mPlaylist;

    //Double Buffering
    private CustomMediaPlayer mPrimaryPlayer;
    private CustomMediaPlayer mNextPlayer;
    private MediaPlayer.OnPreparedListener mPrimaryListener;
    private MediaPlayer.OnPreparedListener mNextListener;
    private MediaPlayer.OnPreparedListener mPreviousListener;

    private boolean shuffle;
    private boolean loop;
    private String state;

    private MusicPlayer(){
        mQueue = new LinkedList<>();
        mPreviousPlayers = new LinkedList<>();

        state = PLAY_VIEW;

        mPrimaryListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayingSongCallback.onTrackDurationReceived(mPrimaryPlayer.getDuration());
                mPrimaryPlayer.setPrepared(true);
                if (mPrimaryPlayer.isWaiting()) {
                    playTrack();
                    mPrimaryPlayer.setWaiting(false);
                }
                playTrack();
                prepareNextPlayer();
            }
        };

        mNextListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mNextPlayer.setPrepared(true);
            }
        };

        mPreviousListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {}
        };

    }

    public static MusicPlayer getInstance(){
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }

        return musicPlayer;
    }

    public void setPlayingSongCallback(PlayingSongCallback playingSongCallback) {
        mPlayingSongCallback = playingSongCallback;
    }

    @Override
    public void onNextTrackClicked() {
        if (mPrimaryPlayer.isPrepared())
            pauseTrack();

        //Afegim la canço que sonava ara a les cançons previes
        if (mPreviousPlayers.size() >= PREVIOUS_TRACKS_BUFFER_SIZE) {
            mPreviousPlayers.removeFirst();
        }
        mPrimaryPlayer.setWaiting(false);
        mPrimaryPlayer.setOnPreparedListener(mPreviousListener);
        mPreviousPlayers.push(mPrimaryPlayer);

        //Fem el canvi de canço, si ha passat una estona ja estara preparada
        if (mNextPlayer != null) {
            mPrimaryPlayer = mNextPlayer;
            mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
            prepareNextPlayer();
            mPlayingSongCallback.onChangedTrack(mPrimaryPlayer.getTrack(), mPrimaryPlayer.getPlaylist());
            mPlayingSongCallback.onTrackDurationReceived(mPrimaryPlayer.getDuration());
            if (mPrimaryPlayer.isPrepared())
                mPrimaryPlayer.seekTo(0);
            playTrack();

        } else {
            mPlayingSongCallback.onPauseTrack();
        }
    }

    @Override
    public void onPreviousTrackClicked() {
        if (mPrimaryPlayer.isPrepared())
            pauseTrack();

        if (!mPreviousPlayers.isEmpty()) {
            mNextPlayer = mPrimaryPlayer;
            mNextPlayer.setWaiting(false);
            mNextPlayer.setOnPreparedListener(mNextListener);
            mPrimaryPlayer = mPreviousPlayers.pop();
            mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
            mPlayingSongCallback.onChangedTrack(mPrimaryPlayer.getTrack(), mPrimaryPlayer.getPlaylist());
            mPlayingSongCallback.onTrackDurationReceived(mPrimaryPlayer.getDuration());
            if (mPrimaryPlayer.isPrepared())
                mPrimaryPlayer.seekTo(0);

            playTrack();

        } else {
            mPlayingSongCallback.onPauseTrack();
        }

    }

    @Override
    public void onShuffleClicked() {

    }

    @Override
    public void onLoopClicked() {

    }

    @Override
    public void onNewTrackClicked(Track track, Playlist playlist) {
        if (mPrimaryPlayer != null && mPrimaryPlayer.isPrepared())
            mPrimaryPlayer.pause();
        state = PLAY_VIEW;
        Playlist playlistAux;

        if (playlist != null && playlist.getTracks() != null) {
            mCurrentPlaylistTrack = getIndexOnPlaylist(track, playlist);

            if (mCurrentPlaylistTrack != -1) {
                mPlaylist = playlist;
            } else{
                mPlaylist = null;
            }

        } else {
            mPlaylist = null;
        }

        mPrimaryPlayer = new CustomMediaPlayer(track, mPlaylist);
        mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
        mPrimaryPlayer.setPrepared(false);
        preparePlayer(mPrimaryPlayer);
    }


    @Override
    public void onPlayPauseClicked() {

        if (state.equals(PLAY_VIEW)) {
            playTrack();

        } else {
            pauseTrack();
        }

    }

    @Override
    public void onProgressChanged(int progress) {
        if (mPrimaryPlayer != null && mPrimaryPlayer.isPrepared()){
            if (Math.abs(mPrimaryPlayer.getCurrentPosition() - progress) > 100) {
                mPrimaryPlayer.seekTo(progress);
            }
        }
    }

    private void playTrack() {
        if(mPrimaryPlayer != null && mPrimaryPlayer.isPrepared()) {
            state = PAUSE_VIEW;
            mPlayingSongCallback.onPlayTrack();
            mPrimaryPlayer.start();

        } else {
            mPrimaryPlayer.setWaiting(true);
        }
    }

    private void pauseTrack() {
        if(mPrimaryPlayer != null && mPrimaryPlayer.isPrepared()) {
            mPrimaryPlayer.pause();
        }
        state = PLAY_VIEW;
        mPlayingSongCallback.onPauseTrack();
    }

    private void prepareNextPlayer() {
        Playlist playlist;
        Track track;

        if (!mQueue.isEmpty()) {
            playlist = new Playlist();
            playlist.setName("Queue");
            track = mQueue.element();

        } else if (mPlaylist != null) {
            playlist = mPlaylist;
            mCurrentPlaylistTrack = (mCurrentPlaylistTrack + 1) % mPlaylist.getTracks().size();
            track =  mPlaylist.getTracks().get(mCurrentPlaylistTrack);

        } else {
            mNextPlayer = null;
            return;
        }

        mNextPlayer = new CustomMediaPlayer(track, playlist);
        mNextPlayer.setOnPreparedListener(mNextListener);
        mNextPlayer.setPrepared(false);
        preparePlayer(mNextPlayer);
    }

    private void preparePlayer(final CustomMediaPlayer player) {
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    player.reset();
                    player.setDataSource(player.getTrack().getUrl());
                    player.prepare();
                } catch (IOException e) {
                    mPlayingSongCallback.onErrorPreparingMediaPlayer();
                }
            }
        });
        connection.start();
    }


    private int getIndexOnPlaylist(Track targetTrack, Playlist playlist) {
        int i = 0;

        for (Track track: playlist.getTracks()) {
            if (track.getName().equals(targetTrack.getName())){
                return i;
            }
            i++;
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (mPrimaryPlayer != null && mPrimaryPlayer.isPrepared()) {
            return mPrimaryPlayer.getCurrentPosition();
        }
        return 0;
    }

    public boolean isPlaying() {
        return state.equals(PAUSE_VIEW);
    }
}
