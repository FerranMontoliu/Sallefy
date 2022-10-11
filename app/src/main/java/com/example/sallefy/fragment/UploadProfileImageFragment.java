
package com.example.sallefy.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentUploadProfileImageBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.network.callback.UpdateUserCallback;
import com.example.sallefy.viewmodel.CreatePlaylistViewModel;
import com.example.sallefy.viewmodel.UploadProfileImageViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class UploadProfileImageFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentUploadProfileImageBinding binding;
    private UploadProfileImageViewModel uploadProfileImageViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadProfileImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadProfileImageViewModel = new ViewModelProvider(this, viewModelFactory).get(UploadProfileImageViewModel.class);

        initViews();

        subscribeObserver();
    }

    private void initViews() {
        binding.uploadPhotoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, CreatePlaylistViewModel.PICK_IMAGE);
        });

        binding.cancelBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.addBtn.setOnClickListener(v -> {
            Toast.makeText(getContext(), R.string.uploading_profile_image, Toast.LENGTH_LONG).show();
            uploadProfileImageViewModel.initCloudinaryManager(requireContext());

            uploadProfileImageViewModel.uploadPhoto(new UpdateUserCallback() {
                @Override
                public void onUserUpdated() {
                    NavHostFragment.findNavController(UploadProfileImageFragment.this).popBackStack();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Toast.makeText(getContext(), R.string.error_uploading_photo, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void subscribeObserver() {
        uploadProfileImageViewModel.getOwnUser().observe(getViewLifecycleOwner(), user -> {
            Glide.with(requireContext())
                    .asBitmap()
                    .placeholder(R.drawable.ic_user_thumbnail)
                    .load(uploadProfileImageViewModel.getUser().getImageUrl())
                    .into(binding.photoIv);
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case UploadProfileImageViewModel.PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data.getData() != null) {
                            uploadProfileImageViewModel.setPhotoUri(data.getData());
                            String path = data.getData().getPath();
                            if (path != null) {
                                final String filename = path.substring(path.lastIndexOf("/") + 1);
                                final InputStream imageStream = requireActivity().getContentResolver().openInputStream(data.getData());
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                                Glide.with(requireContext())
                                        .asBitmap()
                                        .placeholder(R.drawable.ic_user_thumbnail)
                                        .load(selectedImage)
                                        .into(binding.photoIv);

                                uploadProfileImageViewModel.setPhotoFileName(filename);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
}