package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.databinding.FragmentYourLibraryFollowersBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.YourLibraryFollowersViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryFollowersFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryFollowersBinding binding;
    private YourLibraryFollowersViewModel yourLibraryFollowersViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryFollowersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryFollowersViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryFollowersViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {

    }
}