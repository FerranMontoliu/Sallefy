package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.databinding.FragmentPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.PlaylistViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PlaylistFragment extends DaggerFragment {

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
            requireActivity().getIntent().putExtra("clickedUser", ProfileFragmentArgs.fromBundle(getArguments()).getUser());
        }

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {

    }
}
