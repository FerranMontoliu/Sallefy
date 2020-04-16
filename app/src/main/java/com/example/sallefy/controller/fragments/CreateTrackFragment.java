package com.example.sallefy.controller.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sallefy.R;

public class CreateTrackFragment extends Fragment {

    public static final String TAG = CreateTrackFragment.class.getName();

    public static CreateTrackFragment getInstance() {
        return new CreateTrackFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_track, container, false);

        // TODO

        return v;
    }
}