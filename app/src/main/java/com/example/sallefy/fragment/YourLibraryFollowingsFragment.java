package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.sallefy.databinding.FragmentYourLibraryFollowingsBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.viewmodel.YourLibraryFollowingsViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class YourLibraryFollowingsFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentYourLibraryFollowingsBinding binding;
    private YourLibraryFollowingsViewModel yourLibraryFollowingsViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentYourLibraryFollowingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        yourLibraryFollowingsViewModel = new ViewModelProvider(this, viewModelFactory).get(YourLibraryFollowingsViewModel.class);

        initViews();

        subscribeObservers();
    }

    private void initViews() {

    }

    private void subscribeObservers() {

    }
}
