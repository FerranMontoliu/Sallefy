package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.databinding.FragmentAddTrackToPlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.AddTrackToPlaylistViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class AddTrackToPlaylistFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentAddTrackToPlaylistBinding binding;
    private AddTrackToPlaylistViewModel addTrackToPlaylistViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTrackToPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTrackToPlaylistViewModel = new ViewModelProvider(this, viewModelFactory).get(AddTrackToPlaylistViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {

    }
}
