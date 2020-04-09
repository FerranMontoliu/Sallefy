package com.example.sallefy.controller.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;

public class PlayingSongActivity extends AppCompatActivity implements PlaylistAdapterCallback, PlayingSongCallback {
    private ImageButton btnBack;
    private ImageButton btnAdd;
    private TextView tvSongName;
    private TextView tvArtistName;
    private TextView tvPlaylistName;
    private ImageView ivSongImage;
    private ImageButton ibPlayPause;
    private FragmentManager mFragmentManager;
    private Track track;
    private Playlist playlist;
    private MusicPlayer mMusicPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        track = (Track)getIntent().getSerializableExtra("track");
        playlist = (Playlist)getIntent().getSerializableExtra("playlist");

        setContentView(R.layout.activity_playing_song);
        mMusicPlayer = MusicPlayer.getInstance(PlayingSongActivity.this);
        mMusicPlayer.onNewTrackClicked(track, playlist);
        initViews();
    }

    private void initViews() {
        mFragmentManager = getSupportFragmentManager();


        btnBack = findViewById(R.id.back_song);
        btnAdd = findViewById(R.id.add_song);
        ibPlayPause = findViewById(R.id.aps_play_pause_ib);
        tvSongName = findViewById(R.id.aps_song_name);
        tvPlaylistName = findViewById(R.id.aps_playlist_name_tv);
        tvArtistName = findViewById(R.id.aps_artist_name);
        ivSongImage = findViewById(R.id.aps_song_image_iv);

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
    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        //TODO: Add song to playlist
        Toast.makeText(getApplicationContext(), "Song added to " + playlist.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getApplicationContext(),"Error, couldn't play the music.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackDurationReceived(int duration) {

    }

    @Override
    public void onPlayTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_pause_light_80dp);
    }

    @Override
    public void onPauseTrack() {
        ibPlayPause.setImageResource(R.drawable.ic_play_light_80dp);
    }
}
