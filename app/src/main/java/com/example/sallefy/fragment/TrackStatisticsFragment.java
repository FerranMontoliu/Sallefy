package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.sallefy.databinding.FragmentTrackStatisticsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.TrackStatisticsViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class TrackStatisticsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentTrackStatisticsBinding binding;
    private TrackStatisticsViewModel trackStatisticsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrackStatisticsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackStatisticsViewModel = new ViewModelProvider(this, viewModelFactory).get(TrackStatisticsViewModel.class);

        if (getArguments() != null) {
            trackStatisticsViewModel.setTrack(TrackStatisticsFragmentArgs.fromBundle(getArguments()).getTrack());
        }

        initViews();
    }

    private void initViews() {
        binding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}
