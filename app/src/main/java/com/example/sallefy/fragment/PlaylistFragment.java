package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.activity.MainActivity;
import com.example.sallefy.adapter.TrackListAdapter;
import com.example.sallefy.adapter.callback.LikeableListAdapter;
import com.example.sallefy.callback.PlayingSongCallback;
import com.example.sallefy.databinding.FragmentPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.model.Track;
import com.example.sallefy.utils.MusicPlayer;
import com.example.sallefy.viewmodel.PlaylistViewModel;

import java.util.Random;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PlaylistFragment extends DaggerFragment implements PlayingSongCallback, LikeableListAdapter {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentPlaylistBinding binding;
    private PlaylistViewModel playlistViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistViewModel = new ViewModelProvider(this, viewModelFactory).get(PlaylistViewModel.class);

        if (getArguments() != null) {
            playlistViewModel.setPlaylist(PlaylistFragmentArgs.fromBundle(getArguments()).getPlaylist());
        }

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        /*for (int i = 0; i  < tracks.size(); i++){
            TrackManager.getInstance(getContext()).checkLiked(tracks.get(i), PlaylistFragment.this, i);
        }*/

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.playlistTracksRv.setLayoutManager(manager);
        TrackListAdapter adapter = new TrackListAdapter(getContext(), PlaylistFragment.this);
        adapter.setTracks(playlistViewModel.getPlaylist().getTracks());
        binding.playlistTracksRv.setAdapter(adapter);

        binding.playlistName.setText(playlistViewModel.getPlaylist().getName());
        binding.playlistName.setSelected(true);
        binding.playlistAuthor.setText(playlistViewModel.getPlaylist().getUser().getLogin());

        if (playlistViewModel.getPlaylist().getThumbnail() != null) {
            Glide.with(getContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack_60dp)
                    .load(playlistViewModel.getPlaylist().getThumbnail())
                    .into(binding.playlistThumbnail);
        }

        binding.playlistBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*FragmentManager fm = getFragmentManager();
                assert fm != null;
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }*/
            }
        });

        binding.playlistAddSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AddTrackToPlaylistFragment addTrackToPlaylistFragment = new AddTrackToPlaylistFragment();
                Bundle args = new Bundle();
                args.putSerializable("playlist", mPlaylist);
                addTrackToPlaylistFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, addTrackToPlaylistFragment)
                        .remove(PlaylistFragment.this)
                        .addToBackStack(null)
                        .commit();*/
            }
        });

        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isReady() && musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(playlistViewModel.getPlaylist())) {
            binding.playlistShufflePlay.setText(R.string.pause);
        } else {
            binding.playlistShufflePlay.setText(R.string.shuffle_play);
        }

        binding.playlistShufflePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicPlayer musicPlayer = MusicPlayer.getInstance();
                musicPlayer.setShuffle(true);

                if (musicPlayer.isReady()) {

                    if (musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(playlistViewModel.getPlaylist())) {
                        musicPlayer.onPlayPauseClicked();
                        binding.playlistShufflePlay.setText(R.string.shuffle_play);

                    } else {
                        playlistViewModel.newShuffleTrack();
                        binding.playlistShufflePlay.setText(R.string.pause);
                    }

                } else {
                    musicPlayer.setPlayingSongCallback((MainActivity)getActivity());
                    playlistViewModel.newShuffleTrack();
                    binding.playlistShufflePlay.setText(R.string.pause);
                }
            }
        });

        binding.playlistFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PlaylistManager.getInstance(getContext()).followPlaylist(mPlaylist, PlaylistFragment.this);
            }
        });
    }

    private void subscribeObservers() {

    }

    @Override
    public void onErrorPreparingMediaPlayer() {

    }

    @Override
    public void onTrackDurationReceived(int duration) {

    }

    @Override
    public void onPlayTrack() {
        MusicPlayer musicPlayer = MusicPlayer.getInstance();
        if (musicPlayer.isReady() && musicPlayer.isPlaying() && musicPlayer.getCurrentPlaylist().equals(playlistViewModel.getPlaylist())) {
            binding.playlistShufflePlay.setText(R.string.pause);
        } else {
            binding.playlistShufflePlay.setText(R.string.shuffle_play);
        }
    }

    @Override
    public void onPauseTrack() {
        binding.playlistShufflePlay.setText(R.string.shuffle_play);
    }

    @Override
    public void onChangedTrack(Track track, Playlist playlist) {

    }

    @Override
    public void onItemSelected(Object item) {
        if (item instanceof Track)
            MusicPlayer.getInstance().onSetNextTrack((Track) item, playlistViewModel.getPlaylist());
    }

    @Override
    public void onItemLiked(Object item, int position) {
        //TrackManager.getInstance(getContext()).likeTrack(track, PlaylistFragment.this, position);
    }

    @Override
    public void onItemMore(Object item) {
        /*FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(TrackOptionsFragment.TAG);
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up);
        transaction.addToBackStack(null);
        DialogFragment dialogFragment = TrackOptionsFragment.getInstance(track);
        dialogFragment.show(transaction, TrackOptionsFragment.TAG);*/
    }
}
