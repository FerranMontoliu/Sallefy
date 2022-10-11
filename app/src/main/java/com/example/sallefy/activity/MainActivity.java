package com.example.sallefy.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.callback.PlayingSongCallback;
import com.example.sallefy.databinding.ActivityMainBinding;
import com.example.sallefy.fragment.PlaylistFragment;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.objectbox.ObjectBox;
import com.example.sallefy.utils.MusicPlayer;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements PlayingSongCallback {

    private boolean mDisplayPlaying;
    private MusicPlayer mMusicPlayer;
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.NoTitle);
        super.onCreate(savedInstanceState);
        ObjectBox.init(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        mMusicPlayer = MusicPlayer.getInstance();
        mMusicPlayer.setPlayingSongCallback(MainActivity.this);
        mDisplayPlaying = mMusicPlayer.isReady();

        binding.mainPlay.setOnClickListener(v -> {
            if (mMusicPlayer.isReady())
                mMusicPlayer.onPlayPauseClicked();
        });

        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);

        binding.mainPlayingSong.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("track", mMusicPlayer.getCurrentTrack());
            bundle.putSerializable("playlist", mMusicPlayer.getCurrentPlaylist());

            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.playingSongFragment, bundle);
        });

        // Open shared link
        Intent intent = getIntent();
        if (intent == null)
            return;

        Uri uri = intent.getData();
        if (uri == null)
            return;

        String path = uri.getPath();
        if (path == null)
            return;

        String data = path.substring(path.lastIndexOf("/") + 1);
        System.out.println(path);
        System.out.println(data);

        if (path.contains("track"))
            intent.putExtra("sharedTrack", data);

        else if (path.contains("playlist"))
            intent.putExtra("sharedPlaylist", data);

        else if (path.contains("user"))
            intent.putExtra("sharedUser", data);

        else
            Toast.makeText(getApplicationContext(), R.string.error_bad_url, Toast.LENGTH_LONG).show();

    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public void updateTrack() {

        if (mMusicPlayer.isReady()) {
            if (!mDisplayPlaying) {
                mDisplayPlaying = true;
                binding.mainPlayingSong.setVisibility(ConstraintLayout.VISIBLE);
            }

            Track track = mMusicPlayer.getCurrentTrack();
            binding.mainTrackTitle.setText(track.getName());
            binding.mainTrackTitle.setSelected(true);
            binding.mainAuthorTitle.setText(track.getUser().getLogin());
            binding.mainAuthorTitle.setSelected(true);

            Glide.with(getApplicationContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(track.getThumbnail())
                    .into(binding.mainTrackImage);

            binding.mainPlay.setImageResource(mMusicPlayer.isPlaying() ? R.drawable.ic_pause_light_48dp : R.drawable.ic_play_light_48dp);

            binding.mainProgress.setMax(mMusicPlayer.getDuration());
            updateSeekBar();

            //Set the listener after setting the initial duration and position
            binding.mainProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mMusicPlayer.isReady())
                        mMusicPlayer.onProgressChanged(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } else {
            mDisplayPlaying = false;
            binding.mainPlayingSong.setVisibility(ConstraintLayout.GONE);
        }

    }

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getApplicationContext(), R.string.error_couldnt_play_song, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackDurationReceived(int duration) {
        binding.mainProgress.setMax(duration);
    }

    @Override
    public void onPlayTrack() {
        binding.mainPlay.setImageResource(R.drawable.ic_pause_light_48dp);
        updateTrack();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (fragment instanceof PlaylistFragment) {
            ((PlaylistFragment) fragment).onPlayTrack();
        }
    }

    @Override
    public void onPauseTrack() {
        binding.mainPlay.setImageResource(R.drawable.ic_play_light_48dp);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (fragment instanceof PlaylistFragment) {
            ((PlaylistFragment) fragment).onPlayTrack();
        }
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {
        updateTrack();
    }

    public void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable;
        binding.mainProgress.setProgress(mMusicPlayer.getCurrentPosition());

        if (mMusicPlayer.isPlaying()) {
            runnable = this::updateSeekBar;
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
