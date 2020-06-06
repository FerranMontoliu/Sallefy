package com.example.sallefy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.activity.MainActivity;
import com.example.sallefy.callback.PlayingSongCallback;
import com.example.sallefy.databinding.FragmentPlayingSongBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.utils.MusicPlayer;
import com.example.sallefy.viewmodel.PlayingSongViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class PlayingSongFragment extends DaggerFragment implements PlayingSongCallback {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentPlayingSongBinding binding;
    private PlayingSongViewModel playingSongViewModel;
    private MusicPlayer mMusicPlayer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayingSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMusicPlayer = MusicPlayer.getInstance();
        playingSongViewModel = new ViewModelProvider(this, viewModelFactory).get(PlayingSongViewModel.class);

        if (getArguments() != null) {
            playingSongViewModel.setTrack(PlayingSongFragmentArgs.fromBundle(getArguments()).getTrack());
            playingSongViewModel.setPlaylist(PlayingSongFragmentArgs.fromBundle(getArguments()).getPlaylist());
        }

        hideBottom();

        initViews();

        subscribeObservers();
    }

    private void hideBottom() {
        com.example.sallefy.databinding.ActivityMainBinding activityBinding = ((MainActivity) requireActivity()).getBinding();
        activityBinding.bottomNavigation.setVisibility(View.GONE);
        activityBinding.mainPlayingSong.setVisibility(View.GONE);
    }

    private void initViews() {
        binding.apsSongName.setSelected(true);
        binding.apsPlaylistNameTv.setSelected(true);

        binding.apsSongName.setText(playingSongViewModel.getTrack().getName());
        binding.apsArtistName.setText(playingSongViewModel.getTrack().getUser().getLogin());
        binding.apsPlaylistNameTv.setText(playingSongViewModel.getPlaylist().getName());
/*
        TrackManager.getInstance(getApplicationContext()).checkLiked(track, PlayingSongActivity.this, 0);
*/
        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(playingSongViewModel.getTrack().getThumbnail())
                .into(binding.songThumbnail);

        binding.trackOptions.setOnClickListener(v -> {
            //TODO: navigation controller not detected well
            PlayingSongFragmentDirections.ActionPlayingSongFragmentToTrackOptionsFragment action =
                    PlayingSongFragmentDirections.actionPlayingSongFragmentToTrackOptionsFragment();
            action.setTrack(playingSongViewModel.getTrack());
            //NavController navController = Navigation.findNavController(v);
            Navigation.findNavController(v).navigate(action);
        });

        binding.backSong.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });

        binding.apsLikeIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Like Song
                /*
                TrackManager.getInstance(getApplicationContext()).likeTrack(track, PlayingSongActivity.this, 0);
                */
            }
        });

        binding.apsPlayPauseIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onPlayPauseClicked();
            }
        });

        binding.apsNextIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onNextTrackClicked();
            }
        });

        binding.apsPrevIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicPlayer.onPreviousTrackClicked();
            }
        });

        binding.apsPlayPauseIb.setImageResource(mMusicPlayer.isPlaying() ? R.drawable.ic_pause_light_80dp : R.drawable.ic_play_light_80dp);
        binding.apsProgressSb.setMax(mMusicPlayer.getDuration());
        updateSeekBar();

        binding.apsProgressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

        binding.apsLoopIb.setImageResource(MusicPlayer.getInstance().isLoop() ? R.drawable.ic_repeat_green_28dp : R.drawable.ic_repeat_light_28dp);
        binding.apsLoopIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.apsLoopIb.setImageResource(MusicPlayer.getInstance().isLoop() ? R.drawable.ic_repeat_light_28dp : R.drawable.ic_repeat_green_28dp);
                MusicPlayer.getInstance().onLoopClicked();
            }
        });

        binding.apsShuffleIb.setImageResource(MusicPlayer.getInstance().isShuffle() ? R.drawable.ic_shuffle_green_28dp : R.drawable.ic_shuffle_light_28dp);
        binding.apsShuffleIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.apsShuffleIb.setImageResource(MusicPlayer.getInstance().isShuffle() ? R.drawable.ic_shuffle_light_28dp : R.drawable.ic_shuffle_green_28dp);
                MusicPlayer.getInstance().onShuffleClicked();
            }
        });


    }

    public void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable;
        binding.apsProgressSb.setProgress(mMusicPlayer.getCurrentPosition());

        if (mMusicPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private void subscribeObservers() {

    }

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getContext(), "Error, couldn't play the music.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTrackDurationReceived(int duration) {
        binding.apsProgressSb.setProgress(0);
        binding.apsProgressSb.setMax(duration);
        updateSeekBar();

    }

    @Override
    public void onPlayTrack() {
        binding.apsPlayPauseIb.setImageResource(R.drawable.ic_pause_light_80dp);
        updateSeekBar();

    }

    @Override
    public void onPauseTrack() {
        binding.apsPlayPauseIb.setImageResource(R.drawable.ic_play_light_80dp);
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {
        binding.apsSongName.setText(track.getName());
        binding.apsArtistName.setText(track.getUser().getLogin());
        binding.apsPlaylistNameTv.setText(playlist.getName());

        /*
        //TODO: Check liked
        TrackManager.getInstance(getContext()).checkLiked(track, PlayingSongActivity.this, 0);
         */

        Glide.with(getContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(track.getThumbnail())
                .into(binding.songThumbnail);

        updateSeekBar();
    }
}
