package com.example.sallefy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sallefy.databinding.FragmentCreateTrackBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.Track;
import com.example.sallefy.viewmodel.CreateTrackViewModel;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class CreateTrackFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentCreateTrackBinding binding;
    private CreateTrackViewModel createTrackViewModel;

    private static final int PICK_IMAGE = 0;
    private static final int PICK_FILE = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateTrackBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createTrackViewModel = new ViewModelProvider(this, viewModelFactory).get(CreateTrackViewModel.class);

        initButtons();
        initViews();

        subscribeObservers();
    }

    private void initButtons(){
        binding.addBtn.setOnClickListener(v -> {
                //Track newTrack =
        });

        binding.cancelBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.uploadTrackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*");
            startActivityForResult(intent, PICK_FILE);
        });

        binding.uploadThumbnailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

    }

    private void initViews() {

    }

    private void subscribeObservers() {
        createTrackViewModel.getAllGenres().observe(getViewLifecycleOwner(), genres -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genres);
            binding.genreSpinner.setAdapter(adapter);
        });
    }
}
