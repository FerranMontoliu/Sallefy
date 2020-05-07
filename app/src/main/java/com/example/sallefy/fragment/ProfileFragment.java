package com.example.sallefy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentProfileBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.utils.NavigationFixer;
import com.example.sallefy.viewmodel.ProfileViewModel;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this, viewModelFactory).get(ProfileViewModel.class);

        if (getArguments() != null)
            profileViewModel.setUser(ProfileFragmentArgs.fromBundle(getArguments()).getUser());

        NavController navController = Navigation.findNavController(requireActivity(), R.id.sub_fragment_container_profile);
        NavigationUI.setupWithNavController(binding.profileNavigation, navController);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        NavigationFixer.adjustGravity(binding.profileNavigation);
        NavigationFixer.adjustWidth(binding.profileNavigation);

        binding.profileUsername.setText(profileViewModel.getUserName());

        if (profileViewModel.isOwnUser())
            binding.userFollowBtn.setVisibility(View.GONE);

        binding.backProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void subscribeObservers() {
        if (!profileViewModel.isOwnUser()) {
            profileViewModel.isFollowed().observe(getViewLifecycleOwner(), isFollowed -> {
                if (isFollowed != null)
                    if (isFollowed)
                        binding.userFollowBtn.setText(R.string.following);
                    else
                        binding.userFollowBtn.setText(R.string.follow);
            });
        }
    }
}
