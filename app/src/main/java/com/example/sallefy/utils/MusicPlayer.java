package com.example.sallefy.utils;

import android.media.MediaPlayer;
import android.os.Environment;
import android.view.SurfaceHolder;

import com.example.sallefy.callback.MusicPlayerCallback;
import com.example.sallefy.callback.PlayingSongCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.model.Track_;
import com.example.sallefy.objectbox.ObjectBox;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class MusicPlayer implements MusicPlayerCallback {
    // Max previous tracks we will have on buffer
    private static final int PREVIOUS_TRACKS_BUFFER_SIZE = 20;

    // Music and video players
    private static MusicPlayer musicPlayer = null;
    private SurfaceHolder vidHolder;

    // Playing song callback
    private static PlayingSongCallback playingSongCallback;

    // Player state
    private static final String PAUSED = "paused";
    private static final String PLAYING = "playing";
    private String playerState;

    // Future and previous songs
    private LinkedList<Track> nextSongsList;
    private LinkedList<CustomMediaPlayer> previousPlayersList;

    // Playlist info - If no more songs in the queue, it will continue where it left on the playlist
    private int currentPlaylistTrack;
    private Playlist mCurrentPlaylist;

    //Double Buffering
    private CustomMediaPlayer currentPlayer;
    private CustomMediaPlayer nextPlayer;
    private MediaPlayer.OnPreparedListener mainListener;
    private MediaPlayer.OnPreparedListener defaultListener;

    // Player options (shuffle and loop)
    private boolean isShuffleEnabled;
    private boolean isLoopEnabled;

    // To know if we can play the next song yet
    private boolean isNextSongReady;

    private MusicPlayer() {
        // Init parameters
        playerState = PAUSED;
        isNextSongReady = false;
        isShuffleEnabled = false;
        isLoopEnabled = false;
        nextSongsList = new LinkedList<>();
        previousPlayersList = new LinkedList<>();

        mainListener = mp -> {
            playingSongCallback.onTrackDurationReceived(currentPlayer.getDuration());
            playingSongCallback.onChangedTrack(currentPlayer.getTrack(), currentPlayer.getPlaylist());
            currentPlayer.setPrepared(true);

            if (currentPlayer.isWaiting()) {
                currentPlayer.setWaiting(false);
            }

            playTrack();

            if (!isNextSongReady) {
                prepareNextPlayer();
            } else {
                isNextSongReady = false;
            }
        };

        this.defaultListener = mp -> ((CustomMediaPlayer) mp).setPrepared(true);
    }

    // Singleton
    public static MusicPlayer getInstance() {
        if (musicPlayer == null) {
            musicPlayer = new MusicPlayer();
        }

        return musicPlayer;
    }

    @Override
    public void onNextTrackClicked() {
        if (currentPlayer.isPrepared()) {
            pauseTrack();
        }

        if (previousPlayersList.size() >= PREVIOUS_TRACKS_BUFFER_SIZE) {
            previousPlayersList.removeFirst();
        }
        currentPlayer.setWaiting(false);
        currentPlayer.reset();
        if (currentPlayer.getTrack().hasVideo()) {
            currentPlayer.setPrepared(false);
        }

        currentPlayer.setOnPreparedListener(defaultListener);
        previousPlayersList.add(currentPlayer.clone());

        // If there are no more songs to play, pause the track
        if (nextPlayer == null) {
            playingSongCallback.onPauseTrack();
            return;
        }

        // If the next player song is in the queue (index = -1)
        // Removes the first element of the next songs list
        if (nextPlayer.getCurrentPlaylistTrack() == -1) {
            nextSongsList.remove();
        }

        // Set the next player as the current player
        currentPlayer = nextPlayer;
        isNextSongReady = false;
        currentPlayer.setOnPreparedListener(mainListener);
        currentPlaylistTrack = currentPlayer.getCurrentPlaylistTrack() != -1 ?
                currentPlayer.getCurrentPlaylistTrack() :
                currentPlaylistTrack;

        if (currentPlayer.isPrepared()) {
            currentPlayer.seekTo(0);
            playingSongCallback.onTrackDurationReceived(currentPlayer.getDuration());
            playingSongCallback.onChangedTrack(currentPlayer.getTrack(), currentPlayer.getPlaylist());
        } else if (!currentPlayer.isPreparing()) {
            preparePlayer(currentPlayer);
        }

        playTrack();
        prepareNextPlayer();
    }

    @Override
    public void onPreviousTrackClicked() {
        // Reset the progress of the song to 0 and pause
        if (currentPlayer.isPrepared()) {
            currentPlayer.seekTo(0);
            pauseTrack();
        }

        // If no previous songs to play, call pause callback
        if (previousPlayersList == null || previousPlayersList.isEmpty()) {
            playingSongCallback.onPauseTrack();
            return;
        }

        // Set the current player as the next player
        nextPlayer = currentPlayer.clone();
        nextPlayer.setWaiting(false);
        nextPlayer.reset();

        // If the song has video, prepare video holder
        if (nextPlayer.getTrack().hasVideo()) {
            nextPlayer.setPrepared(false);
        }

        nextPlayer.setOnPreparedListener(defaultListener);

        // Gets the last (most recent) previous player and assigns it to the current player
        isNextSongReady = true;
        currentPlayer = previousPlayersList.removeLast().clone();
        currentPlayer.setOnPreparedListener(mainListener);
        currentPlaylistTrack = currentPlayer.getCurrentPlaylistTrack() != -1 ?
                currentPlayer.getCurrentPlaylistTrack() :
                currentPlaylistTrack;

        if (currentPlayer.isPrepared()) {
            currentPlayer.seekTo(0);
            playingSongCallback.onTrackDurationReceived(currentPlayer.getDuration());
            playingSongCallback.onChangedTrack(currentPlayer.getTrack(), currentPlayer.getPlaylist());
        } else if (!currentPlayer.isPreparing()) {
            preparePlayer(currentPlayer);
        }

        playTrack();
    }

    @Override
    public void onShuffleClicked() {
        isShuffleEnabled = !isShuffleEnabled;
        prepareNextPlayer();
    }

    @Override
    public void onLoopClicked() {
        isLoopEnabled = !isLoopEnabled;
    }

    @Override
    public void onNewTrackClicked(Track track, Playlist playlist) {
        if (currentPlayer != null && currentPlayer.isPrepared()) {
            currentPlayer.pause();
        }
        playerState = PAUSED;

        if (playlist != null && playlist.getTracks() != null) {
            currentPlaylistTrack = getIndexOnPlaylist(track, playlist);

            if (currentPlaylistTrack != -1) {
                mCurrentPlaylist = playlist;
            } else {
                mCurrentPlaylist = null;
            }
        } else {
            mCurrentPlaylist = null;
        }

        currentPlayer = new CustomMediaPlayer(track, mCurrentPlaylist, currentPlaylistTrack);
        currentPlayer.setOnPreparedListener(mainListener);
        currentPlayer.setPrepared(false);
        preparePlayer(currentPlayer);
    }

    @Override
    public void onPlayPauseClicked() {
        if (playerState.equals(PAUSED)) {
            playTrack();
        } else {
            pauseTrack();
        }
    }

    @Override
    public void onProgressChanged(int progress) {
        if (currentPlayer != null &&
                currentPlayer.isPrepared() &&
                Math.abs(currentPlayer.getCurrentPosition() - progress) > 500) {
            currentPlayer.seekTo(progress);
        }
    }

    @Override
    public void onSetNextTrack(Track track, Playlist playlist) {
        if (currentPlayer == null) {
            this.onNewTrackClicked(track, playlist);
        } else {
            if (currentPlayer.isPrepared()) {
                currentPlayer.pause();
            }

            if (previousPlayersList.size() >= PREVIOUS_TRACKS_BUFFER_SIZE) {
                previousPlayersList.removeFirst();
            }

            currentPlayer.setWaiting(false);
            currentPlayer.setOnPreparedListener(defaultListener);
            previousPlayersList.add(currentPlayer);

            playerState = PAUSED;

            if (playlist != null && playlist.getTracks() != null) {
                currentPlaylistTrack = getIndexOnPlaylist(track, playlist);

                if (currentPlaylistTrack != -1) {
                    mCurrentPlaylist = playlist;
                } else {
                    mCurrentPlaylist = null;
                }
            } else {
                mCurrentPlaylist = null;
            }

            currentPlayer = new CustomMediaPlayer(track, mCurrentPlaylist, currentPlaylistTrack);
            currentPlayer.setOnPreparedListener(mainListener);
            currentPlayer.setPrepared(false);
            preparePlayer(currentPlayer);
        }
    }

    private void playTrack() {
        if (currentPlayer == null) {
            return;
        }

        if (currentPlayer.isPrepared()) {
            currentPlayer.start();
            playerState = PLAYING;
            playingSongCallback.onPlayTrack();
        } else {
            currentPlayer.setWaiting(true);
        }
    }

    private void pauseTrack() {
        if (currentPlayer != null && currentPlayer.isPrepared()) {
            currentPlayer.pause();
            playerState = PAUSED;
            playingSongCallback.onPauseTrack();
        }
    }

    private void prepareNextPlayer() {
        Playlist playlist;
        Track track;
        int trackIndex = -1;

        if (!nextSongsList.isEmpty()) {
            playlist = new Playlist();
            playlist.setName("queue");
            track = nextSongsList.peek();
        } else if (mCurrentPlaylist != null) {
            playlist = mCurrentPlaylist;

            if (isShuffleEnabled) {
                trackIndex = currentPlaylistTrack;
                Random r = new Random();
                while (trackIndex == currentPlaylistTrack) {
                    trackIndex = r.nextInt(mCurrentPlaylist.getTracks().size());
                }
            } else {
                trackIndex = (currentPlaylistTrack + 1) % mCurrentPlaylist.getTracks().size();
            }

            track = mCurrentPlaylist.getTracks().get(trackIndex);
        } else {
            nextPlayer = null;
            return;
        }

        if (nextPlayer != null &&
                nextPlayer.isPrepared() &&
                nextPlayer.getTrack() == track &&
                nextPlayer.getPlaylist() == playlist) {
            return;
        }

        nextPlayer = new CustomMediaPlayer(track, playlist, trackIndex);
        nextPlayer.setOnPreparedListener(defaultListener);
        nextPlayer.setPrepared(false);
        preparePlayer(nextPlayer);
    }

    private void preparePlayer(final CustomMediaPlayer player) {
        Thread connection = new Thread(() -> {
            try {
                if (player == currentPlayer || !player.getTrack().hasVideo()) {
                    player.setPreparing(true);
                    player.reset();

                    if (player.getTrack().hasVideo()) {
                        player.setDisplay(vidHolder);
                    }

                    if (ObjectBox.getBoxStore().boxFor(Track.class).query().equal(Track_.id, player.getTrack().getId()).build().count() != 0) {
                        String[] splitUrl = player.getTrack().getUrl().split("/");
                        String path = Environment.getExternalStorageDirectory() + "/" + splitUrl[splitUrl.length - 1];
                        File file = new File(path);
                        if (file.exists()) {
                            player.setDataSource(path);
                        } else {
                            player.setDataSource(player.getTrack().getUrl());
                        }
                    } else {
                        player.setDataSource(player.getTrack().getUrl());
                    }
                    player.prepare();
                }
                player.setPreparing(false);
            } catch (IOException e) {
                e.printStackTrace();
                playingSongCallback.onErrorPreparingMediaPlayer();
            }
        });

        connection.start();
    }

    private int getIndexOnPlaylist(Track targetTrack, Playlist playlist) {
        int i = 0;

        for (Track track : playlist.getTracks()) {
            if (track.getId() == targetTrack.getId()) {
                return i;
            }
            i++;
        }

        return -1;
    }

    public int getCurrentPosition() {
        if (currentPlayer != null && currentPlayer.isPrepared()) {
            return currentPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getCurrentPlaylistTrack() {
        return currentPlaylistTrack;
    }

    public boolean isPlaying() {
        return playerState.equals(PLAYING);
    }

    public boolean isLoopEnabled() {
        return isLoopEnabled;
    }

    public boolean isShuffleEnabled() {
        return isShuffleEnabled;
    }

    public void restartPlayer() {
        if (currentPlayer != null && currentPlayer.isPrepared()) {
            currentPlayer.seekTo(0);
            playTrack();
        }
    }

    public boolean isReady() {
        return currentPlayer != null;
    }

    public Track getCurrentTrack() {
        if (currentPlayer != null) {
            return currentPlayer.getTrack();
        }
        return null;
    }

    public int getDuration() {
        if (currentPlayer != null && currentPlayer.isPrepared()) {
            return currentPlayer.getDuration();
        }
        return 0;
    }

    public Playlist getCurrentPlaylist() {
        if (currentPlayer != null) {
            return currentPlayer.getPlaylist();
        }
        return null;
    }

    public void setShuffleEnabled(boolean shuffleEnabled) {
        this.isShuffleEnabled = shuffleEnabled;

        if (shuffleEnabled) {
            prepareNextPlayer();
        }
    }

    public void addToQueue(Track track) {
        try {
            if (currentPlayer == null) {
                Playlist queuePlaylist = new Playlist();
                queuePlaylist.setName("queue");
                this.onNewTrackClicked(track, queuePlaylist);
            } else {
                nextSongsList.add(track);
                prepareNextPlayer();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void setPlayingSongCallback(PlayingSongCallback playingSongCallback) {
        MusicPlayer.playingSongCallback = playingSongCallback;
    }

    public void updateVidHolder(SurfaceHolder vidHolder) {
        this.vidHolder = vidHolder;

        if (vidHolder != null && currentPlayer != null && currentPlayer.getTrack().hasVideo()) {
            currentPlayer.setDisplay(vidHolder);
        }
    }
}
