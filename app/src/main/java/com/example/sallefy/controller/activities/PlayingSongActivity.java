package com.example.sallefy.controller.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.controller.MusicPlayer;
import com.example.sallefy.controller.callbacks.PlayingSongCallback;
import com.example.sallefy.controller.callbacks.PlaylistAdapterCallback;
import com.example.sallefy.controller.fragments.AddSongPlaylistFragment;
import com.example.sallefy.controller.restapi.callback.PlaylistCallback;
import com.example.sallefy.controller.restapi.manager.PlaylistManager;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Followed;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

import java.util.List;

public class PlayingSongActivity extends AppCompatActivity implements PlaylistAdapterCallback, PlayingSongCallback, PlaylistCallback {
    private ImageButton btnBack;
    private ImageButton btnAdd;
    private TextView tvSongName;
    private TextView tvArtistName;
    private TextView tvPlaylistName;
    private ImageView ivSongImage;
    private ImageButton ibPlayPause;
    private ImageButton ibNext;
    private ImageButton ibPrev;
    private FragmentManager mFragmentManager;
    private Track track;
    private Playlist playlist;
    private MusicPlayer mMusicPlayer;
    private SeekBar mSeekBar;

    private int mDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        track = (Track)getIntent().getSerializableExtra("track");
        playlist = (Playlist)getIntent().getSerializableExtra("playlist");

        setContentView(R.layout.activity_playing_song);
        mMusicPlayer = MusicPlayer.getInstance();
        mMusicPlayer.setPlayingSongCallback(PlayingSongActivity.this);
        mMusicPlayer.onNewTrackClicked(track, playlist);
        initViews();
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();


        btnBack = findViewById(R.id.back_song);
        btnAdd = findViewById(R.id.add_song);
        ibPlayPause = findViewById(R.id.aps_play_pause_ib);
        ibNext = findViewById(R.id.aps_next_ib);
        ibPrev = findViewById(R.id.aps_prev_ib);
        tvSongName = findViewById(R.id.aps_song_name);
        tvPlaylistName = findViewById(R.id.aps_playlist_name_tv);
        tvArtistName = findViewById(R.id.aps_artist_name);
        ivSongImage = findViewById(R.id.aps_song_image_iv);
        mSeekBar = findViewById(R.id.aps_progress_sb);

        tvSongName.setText(track.getName());
        tvArtistName.setText(track.getUser().getLogin());
        tvPlaylistName.setText(playlist.getName());

        Glide.with(getApplicationContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(track.getThumbnail())
                .into(ivSongImage);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();

                Fragment prev = mFragmentManager.findFragmentByTag("dialog");
                if (prev != null) {
                    transaction.remove(prev);
                }
                transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
                transaction.addToBackStack(null);
                DialogFragment dialogFragment = new AddSongPlaylistFragment(PlayingSongActivity.this);
                dialogFragment.show(transaction, "dialog");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onPlayPauseClicked();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onNextTrackClicked();
            }
        });

        ibPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onPreviousTrackClicked();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMusicPlayer.onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        PlaylistManager.getInstance(getApplicationContext()).getPlaylistById(playlist.getId(), PlayingSongActivity.this);
    }

    @Override
    public void onPlaylistReceived(Playlist playlist) {
        TrackManager.getInstance(getApplicationContext()).addTrackToPlaylist(track, playlist, PlayingSongActivity.this);
    }

    @Override
    public void onPlaylistUpdated(Playlist playlist) {
        Toast.makeText(getApplicationContext(), track.getName() + " added to " + playlist.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistNotUpdated(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Error, can not add " + track.getName() + " to the selected playlist.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPlaylistNotReceived(Throwable throwable) {
        Toast.makeText(getApplicationContext(), "Error, can not get the playlist.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getApplicationContext(),"Error, couldn't play the music.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackDurationReceived(int duration) {
        mSeekBar.setMax(duration);
        mDuration = duration;
    }

    @Override
    public void onPlayTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_pause_light_80dp);
        updateSeekBar();
    }

    @Override
    public void onPauseTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_play_light_80dp);
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {
        tvSongName.setText(track.getName());
        tvArtistName.setText(track.getUser().getLogin());
        tvPlaylistName.setText(playlist.getName());

        Glide.with(getApplicationContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(track.getThumbnail())
                .into(ivSongImage);

        mSeekBar.setProgress(0);
        updateSeekBar();
    }

    public void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable;
        int pos = mMusicPlayer.getCurrentPosition();
        mSeekBar.setProgress(mMusicPlayer.getCurrentPosition());

        if(mMusicPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void onPlaylistCreated(Playlist playlist) {

    }

    @Override
    public void onPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistsReceived(List<Playlist> playlists) {

    }

    @Override
    public void onPlaylistsNotReceived(Throwable throwable) {

    }

    @Override
    public void onPlaylistFollowed() {

    }

    @Override
    public void onPlaylistFollowError(Throwable throwable) {

    }

    @Override
    public void onIsFollowedReceived(Followed followed) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
