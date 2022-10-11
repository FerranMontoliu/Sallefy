
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

import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentCreatePlaylistBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.network.callback.CreatePlaylistCallback;
import com.example.sallefy.viewmodel.CreatePlaylistViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class CreatePlaylistFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentCreatePlaylistBinding binding;
    private CreatePlaylistViewModel createPlaylistViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPlaylistViewModel = new ViewModelProvider(this, viewModelFactory).get(CreatePlaylistViewModel.class);

        initViews();
    }

    private void initViews() {
        binding.uploadThumbnailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, CreatePlaylistViewModel.PICK_IMAGE);
        });

        binding.cancelBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.addBtn.setOnClickListener(v -> {
            if (!binding.titleEt.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), R.string.creating_playlist, Toast.LENGTH_LONG).show();
                createPlaylistViewModel.initCloudinaryManager(requireContext());

                createPlaylistViewModel.setPlaylistFileName(binding.titleEt.getText().toString());
                createPlaylistViewModel.uploadPlaylist(new CreatePlaylistCallback() {
                    @Override
                    public void onPlaylistCreated() {
                        NavHostFragment.findNavController(CreatePlaylistFragment.this).popBackStack();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getContext(), R.string.error_playlist_not_created, Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(getContext(), R.string.error_enter_name_playlist, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case CreatePlaylistViewModel.PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data.getData() != null) {
                            createPlaylistViewModel.setThumbnailUri(data.getData());
                            String path = data.getData().getPath();
                            if (path != null) {
                                final String filename = path.substring(path.lastIndexOf("/") + 1);
                                final InputStream imageStream = requireActivity().getContentResolver().openInputStream(data.getData());
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                binding.thumbnailIv.setImageBitmap(selectedImage);
                                createPlaylistViewModel.setThumbnailFileName(filename);
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