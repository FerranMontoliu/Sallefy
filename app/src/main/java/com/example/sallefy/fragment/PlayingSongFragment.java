package com.example.sallefy.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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


public class PlayingSongFragment extends DaggerFragment implements PlayingSongCallback, SurfaceHolder.Callback {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentPlayingSongBinding binding;
    private PlayingSongViewModel playingSongViewModel;
    private MusicPlayer mMusicPlayer;
    private boolean updateSeekBar = false;


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
        mMusicPlayer.setPlayingSongCallback(PlayingSongFragment.this);

        playingSongViewModel = new ViewModelProvider(this, viewModelFactory).get(PlayingSongViewModel.class);

        if (getArguments() != null) {
            playingSongViewModel.setTrack(PlayingSongFragmentArgs.fromBundle(getArguments()).getTrack());
            playingSongViewModel.setPlaylist(PlayingSongFragmentArgs.fromBundle(getArguments()).getPlaylist());
        }

        hideBottom();

        initViews();

        subscribeObservers();

        if (playingSongViewModel.getTrack() != null &&
                playingSongViewModel.getPlaylist() != null &&
                playingSongViewModel.getPlaylist() != mMusicPlayer.getCurrentPlaylist() &&
                playingSongViewModel.getTrack() != mMusicPlayer.getCurrentTrack()) {
            mMusicPlayer.onNewTrackClicked(playingSongViewModel.getTrack(), playingSongViewModel.getPlaylist());
        }
    }

    private void hideBottom() {
        com.example.sallefy.databinding.ActivityMainBinding activityBinding = ((MainActivity) requireActivity()).getBinding();
        activityBinding.bottomNavigation.setVisibility(View.GONE);
        activityBinding.mainPlayingSong.setVisibility(View.GONE);
    }

    private void showBottom() {
        com.example.sallefy.databinding.ActivityMainBinding activityBinding = ((MainActivity) requireActivity()).getBinding();
        activityBinding.bottomNavigation.setVisibility(View.VISIBLE);
        activityBinding.mainPlayingSong.setVisibility(View.VISIBLE);
        mMusicPlayer.setPlayingSongCallback((MainActivity) getActivity());
        ((MainActivity) getActivity()).updateTrack();
    }

    private void displayVideoThumbnail() {
        if (playingSongViewModel.getTrack() != null &&
                playingSongViewModel.getTrack().hasVideo()) {
            binding.songVideo.setVisibility(View.VISIBLE);
            binding.songThumbnail.setVisibility(View.GONE); //Actualitza el video holder per el media player actual
        } else {
            binding.songVideo.setVisibility(View.GONE);
            binding.songThumbnail.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        binding.songVideo.getHolder().addCallback(PlayingSongFragment.this);
        displayVideoThumbnail();

        binding.apsSongName.setText(playingSongViewModel.getTrack().getName());
        binding.apsArtistName.setText(playingSongViewModel.getTrack().getUser().getLogin());
        binding.apsSongName.setSelected(true);
        binding.apsArtistName.setSelected(true);

        if (playingSongViewModel.getPlaylist() != null) {
            binding.apsPlaylistNameTv.setText(playingSongViewModel.getPlaylist().getName());
            binding.apsPlaylistNameTv.setSelected(true);
        }

        Glide.with(requireContext())
                .asBitmap()
                .placeholder(R.drawable.ic_audiotrack_60dp)
                .load(playingSongViewModel.getTrack().getThumbnail())
                .into(binding.songThumbnail);

        binding.trackOptions.setOnClickListener(v -> {
            PlayingSongFragmentDirections.ActionPlayingSongFragmentToTrackOptionsFragment action =
                    PlayingSongFragmentDirections.actionPlayingSongFragmentToTrackOptionsFragment();
            action.setTrack(playingSongViewModel.getTrack());
            Navigation.findNavController(v).navigate(action);
        });

        binding.backSong.setOnClickListener(v -> {
            showBottom();
            Navigation.findNavController(v).popBackStack();
        });

        binding.apsLikeIb.setOnClickListener(v -> {
            playingSongViewModel.likeTrackToggle();
        });

        binding.apsPlayPauseIb.setOnClickListener(v -> {
            mMusicPlayer.onPlayPauseClicked();
        });

        binding.apsNextIb.setOnClickListener(v -> {
            mMusicPlayer.onNextTrackClicked();
        });

        binding.apsPrevIb.setOnClickListener(v -> {
            mMusicPlayer.onPreviousTrackClicked();
        });

        binding.apsPlayPauseIb.setImageResource(mMusicPlayer.isPlaying() ? R.drawable.ic_pause_light_80dp : R.drawable.ic_play_light_80dp);
        binding.apsProgressSb.setMax(mMusicPlayer.getDuration());
        updateSeekBar();

        binding.apsProgressSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (updateSeekBar) {
                    mMusicPlayer.onProgressChanged(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.apsLoopIb.setImageResource(MusicPlayer.getInstance().isLoopEnabled()
                ? R.drawable.ic_repeat_green_28dp
                : R.drawable.ic_repeat_light_28dp);

        binding.apsLoopIb.setOnClickListener(v -> {
            binding.apsLoopIb.setImageResource(MusicPlayer.getInstance().isLoopEnabled()
                    ? R.drawable.ic_repeat_light_28dp
                    : R.drawable.ic_repeat_green_28dp);
            MusicPlayer.getInstance().onLoopClicked();
        });

        binding.apsShuffleIb.setImageResource(MusicPlayer.getInstance().isShuffleEnabled()
                ? R.drawable.ic_shuffle_green_28dp
                : R.drawable.ic_shuffle_light_28dp);

        binding.apsShuffleIb.setOnClickListener(v -> {
            binding.apsShuffleIb.setImageResource(MusicPlayer.getInstance().isShuffleEnabled()
                    ? R.drawable.ic_shuffle_light_28dp
                    : R.drawable.ic_shuffle_green_28dp);
            MusicPlayer.getInstance().onShuffleClicked();
        });
    }

    public void updateSeekBar() {
        Handler handler = new Handler();
        Runnable runnable;
        binding.apsProgressSb.setProgress(mMusicPlayer.getCurrentPosition());

        if (mMusicPlayer.isPlaying()) {
            runnable = this::updateSeekBar;
            handler.postDelayed(runnable, 1000);
        }
    }

    private void subscribeObservers() {
        playingSongViewModel.isLiked().observe(getViewLifecycleOwner(), isLiked -> {
            if (isLiked) {
                binding.apsLikeIb.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_filled, getActivity().getTheme()));
            } else {
                binding.apsLikeIb.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_unfilled, getActivity().getTheme()));
            }
        });
    }

    @Override
    public void onErrorPreparingMediaPlayer() {
        Toast.makeText(getContext(), R.string.error_couldnt_play_song, Toast.LENGTH_LONG).show();
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
        if (playingSongViewModel.getTrack() != track || playingSongViewModel.getPlaylist() != playlist) {
            binding.apsSongName.setText(track.getName());
            binding.apsSongName.setSelected(true);
            binding.apsArtistName.setText(track.getUser().getLogin());
            if (playlist != null) {
                binding.apsPlaylistNameTv.setText(playlist.getName());
            }
            binding.apsArtistName.setSelected(true);
            playingSongViewModel.setTrack(track);
            playingSongViewModel.setPlaylist(playlist);
            displayVideoThumbnail();
            playingSongViewModel.isLiked();

            Glide.with(requireContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(track.getThumbnail())
                    .into(binding.songThumbnail);

            updateSeekBar();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mMusicPlayer.updateVidHolder(binding.songVideo.getHolder());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Unused
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mMusicPlayer.updateVidHolder(null);
    }

    @Override
    public void onResume() {
        updateSeekBar = true;
        super.onResume();
    }

    @Override
    public void onPause() {
        updateSeekBar = false;
        super.onPause();
    }

}
