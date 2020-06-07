package com.example.sallefy.utils;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.sallefy.callback.MusicPlayerCallback;
import com.example.sallefy.callback.PlayingSongCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.objectbox.ObjectBox;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class MusicPlayer implements MusicPlayerCallback {

    private static MusicPlayer musicPlayer = null;

    private SurfaceHolder vidHolder;

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
    private MediaPlayer.OnPreparedListener mDefaultListener;

    private boolean shuffle;
    private boolean loop;
    private String state;
    private boolean nextIsFine;

    private MusicPlayer(){
        nextIsFine = false;
        shuffle = false;
        loop = false;
        mQueue = new LinkedList<>();
        mPreviousPlayers = new LinkedList<>();

        state = PLAY_VIEW;

        mPrimaryListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayingSongCallback.onTrackDurationReceived(mPrimaryPlayer.getDuration());
                mPrimaryPlayer.seekTo(0);
                mPlayingSongCallback.onChangedTrack(mPrimaryPlayer.getTrack(), mPrimaryPlayer.getPlaylist());
                mPrimaryPlayer.setPrepared(true);
                if (mPrimaryPlayer.isWaiting()) {
                    //playTrack();
                    mPrimaryPlayer.setWaiting(false);
                }
                playTrack();
                if (!nextIsFine) {
                    prepareNextPlayer();
                } else {
                    nextIsFine = false;
                }
            }
        };

        mDefaultListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ((CustomMediaPlayer)mp).setPrepared(true);
            }
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

    public void setVidHolder(SurfaceHolder vidHolder) {
        this.vidHolder = vidHolder;
        if (vidHolder != null && mPrimaryPlayer != null && mPrimaryPlayer.getTrack().getHasVideo()) {
            mPrimaryPlayer.setDisplay(vidHolder);
        }
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
        mPrimaryPlayer.setOnPreparedListener(mDefaultListener);
        mPreviousPlayers.push(mPrimaryPlayer);

        //Fem el canvi de canço, si ha passat una estona ja estara preparada
        if (mNextPlayer != null) {

            if (mNextPlayer.getmCurrentPlaylistTrack() == -1)
                mQueue.remove();

            mPrimaryPlayer = mNextPlayer;
            mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
            mCurrentPlaylistTrack = mPrimaryPlayer.getmCurrentPlaylistTrack() != -1 ? mPrimaryPlayer.getmCurrentPlaylistTrack() : mCurrentPlaylistTrack;
            prepareNextPlayer();

            if (mPrimaryPlayer.isPrepared()) {
                mPlayingSongCallback.onTrackDurationReceived(mPrimaryPlayer.getDuration());
                mPlayingSongCallback.onChangedTrack(mPrimaryPlayer.getTrack(), mPrimaryPlayer.getPlaylist());
                restart();
            }
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
            mNextPlayer.setOnPreparedListener(mDefaultListener);

            mPrimaryPlayer = mPreviousPlayers.pop();
            mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
            mCurrentPlaylistTrack = mPrimaryPlayer.getmCurrentPlaylistTrack() != -1 ? mPrimaryPlayer.getmCurrentPlaylistTrack() : mCurrentPlaylistTrack;

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
        shuffle = !shuffle;
        prepareNextPlayer();
    }

    @Override
    public void onLoopClicked() {
        loop = !loop;
    }

    @Override
    public void onNewTrackClicked(Track track, Playlist playlist) {
        if (mPrimaryPlayer != null && mPrimaryPlayer.isPrepared())
            mPrimaryPlayer.pause();
        state = PLAY_VIEW;

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

        mPrimaryPlayer = new CustomMediaPlayer(track, mPlaylist, mCurrentPlaylistTrack);
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
            if (Math.abs(mPrimaryPlayer.getCurrentPosition() - progress) > 500) {
                mPrimaryPlayer.seekTo(progress);
            }
        }
    }

    @Override
    public void onSetNextTrack(Track track, Playlist playlist) {
        if (mPrimaryPlayer == null) {
            this.onNewTrackClicked(track, playlist);

        } else {
            if (mPrimaryPlayer.isPrepared())
                mPrimaryPlayer.pause();

            //Afegim la canço que sonava ara a les cançons previes
            if (mPreviousPlayers.size() >= PREVIOUS_TRACKS_BUFFER_SIZE) {
                mPreviousPlayers.removeFirst();
            }
            mPrimaryPlayer.setWaiting(false);
            mPrimaryPlayer.setOnPreparedListener(mDefaultListener);
            mPreviousPlayers.push(mPrimaryPlayer);

            state = PLAY_VIEW;

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

            mPrimaryPlayer = new CustomMediaPlayer(track, mPlaylist, mCurrentPlaylistTrack);
            mPrimaryPlayer.setOnPreparedListener(mPrimaryListener);
            mPrimaryPlayer.setPrepared(false);
            preparePlayer(mPrimaryPlayer);
            nextIsFine = true;
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
        int trackIndex = -1;

        if (!mQueue.isEmpty()) {
            playlist = new Playlist();
            playlist.setName("Queue");
            track = mQueue.peek();
            trackIndex = -1;

        } else if (mPlaylist != null) {
            playlist = mPlaylist;

            if (shuffle) {
                trackIndex = mCurrentPlaylistTrack;
                Random r = new Random();
                while (trackIndex == mCurrentPlaylistTrack) {
                    trackIndex = r.nextInt(mPlaylist.getTracks().size());
                }
            } else {
                trackIndex = (mCurrentPlaylistTrack + 1) % mPlaylist.getTracks().size();
            }

            track =  mPlaylist.getTracks().get(trackIndex);

        } else {
            mNextPlayer = null;
            return;
        }

        mNextPlayer = new CustomMediaPlayer(track, playlist, trackIndex);
        mNextPlayer.setOnPreparedListener(mDefaultListener);
        mNextPlayer.setPrepared(false);
        preparePlayer(mNextPlayer);
    }

    private void preparePlayer(final CustomMediaPlayer player) {
        Thread connection = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    player.reset();
                    if (vidHolder != null && player.getTrack().getHasVideo()) {
                        player.setDisplay(vidHolder);
                    }
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
            if (Long.toString(track.getId()).equals(Long.toString(targetTrack.getId()))){
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

    public int getCurrentPlaylistTrack() {
        return mCurrentPlaylistTrack;
    }

    public boolean isPlaying() {
        return state.equals(PAUSE_VIEW);
    }

    public boolean isLoop() {
        return loop;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void restart() {
        mPrimaryPlayer.seekTo(0);
        playTrack();
    }

    public boolean isReady() {
        return mPrimaryPlayer != null;
    }

    public Track getCurrentTrack() {
        if (mPrimaryPlayer != null) {
            return mPrimaryPlayer.getTrack();

        } else {
            return null;
        }
    }

    public int getDuration() {
        if (mPrimaryPlayer != null && mPrimaryPlayer.isPrepared())
            return mPrimaryPlayer.getDuration();
        else
            return 0;
    }

    public Playlist getCurrentPlaylist() {
        if (mPrimaryPlayer != null) {
            return mPrimaryPlayer.getPlaylist();
        } else {
            return null;
        }
    }

    public void setShuffle(boolean shuffle) {
        if (!this.shuffle && shuffle) {
            prepareNextPlayer();
        }

        this.shuffle = shuffle;
    }

    public boolean addToQueue(Track track) {
        try {
            if (mPrimaryPlayer == null) {
                Playlist p = new Playlist();
                p.setName("queue");
                this.onNewTrackClicked(track, p);
            } else {
                mQueue.add(track);
                prepareNextPlayer();
            }
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }


}
