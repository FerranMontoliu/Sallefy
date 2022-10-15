package com.example.sallefy.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentProfileBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.utils.BitmapUtils;
import com.example.sallefy.viewmodel.ProfileViewModel;

import java.io.ByteArrayOutputStream;

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

        if (getArguments() != null) {
            profileViewModel.setUser(ProfileFragmentArgs.fromBundle(getArguments()).getUser());
            requireActivity().getIntent().putExtra("clickedUser", ProfileFragmentArgs.fromBundle(getArguments()).getUser());
        }

        NavController navController = Navigation.findNavController(requireActivity(), R.id.sub_fragment_container_profile);
        NavigationUI.setupWithNavController(binding.profileNavigation, navController);

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        binding.backProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        if (profileViewModel.getUserName() != null)
            binding.profileUsername.setText(profileViewModel.getUserName());

        Glide.with(requireContext())
                .asBitmap()
                .placeholder(R.drawable.ic_user_thumbnail)
                .load(profileViewModel.getUserImage())
                .into(binding.profilePhoto);


        if (profileViewModel.isOwnUser())
            binding.userFollowBtn.setVisibility(View.GONE);

        binding.backProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.userFollowBtn.setOnClickListener(v -> {
            profileViewModel.followUserToggle();
        });

        binding.userShare.setOnClickListener(v -> {
            checkForPermissions();
        });
    }

    private void checkForPermissions() {
        int permissionWrite = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRead = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionWrite != PackageManager.PERMISSION_GRANTED || permissionRead != PackageManager.PERMISSION_GRANTED)
            askForPermission();
        else
            shareUserLink();
    }

    private void askForPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void shareUserLink() {
        Bitmap bitmap = BitmapUtils.getBitmapFromView(binding.profilePhoto);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap, "shared_image", null);
        Uri imageUri = Uri.parse(path);
        intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_user_text) + profileViewModel.getUser().getLogin());
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_user_via)));
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
