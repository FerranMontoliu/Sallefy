package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.databinding.FragmentAddSongToPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.AddSongToPlaylistViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddSongToPlaylistFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentAddSongToPlaylistBinding binding;
    private AddSongToPlaylistViewModel addSongToPlaylistViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSongToPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addSongToPlaylistViewModel = new ViewModelProvider(this, viewModelFactory).get(AddSongToPlaylistViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {

    }
}
