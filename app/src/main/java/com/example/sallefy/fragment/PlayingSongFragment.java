package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.activity.MainActivity;
import com.example.sallefy.databinding.FragmentPlayingSongBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.PlayingSongViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class PlayingSongFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentPlayingSongBinding binding;
    private PlayingSongViewModel playingSongViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlayingSongBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playingSongViewModel = new ViewModelProvider(this, viewModelFactory).get(PlayingSongViewModel.class);

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

    }

    private void subscribeObservers() {

    }
}
