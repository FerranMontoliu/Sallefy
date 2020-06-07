
package com.example.sallefy.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.sallefy.R;
import com.example.sallefy.databinding.FragmentCreateTrackBinding;
import com.example.sallefy.factory.ViewModelFactory;
import com.example.sallefy.model.Genre;
import com.example.sallefy.network.callback.CreateTrackCallback;
import com.example.sallefy.viewmodel.CreateTrackViewModel;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static android.app.Activity.RESULT_OK;


public class CreateTrackFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    private FragmentCreateTrackBinding binding;
    private CreateTrackViewModel createTrackViewModel;


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

        initViews();

        subscribeObservers();
    }

    private void initViews() {
        binding.uploadThumbnailBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, CreateTrackViewModel.PICK_IMAGE);
        });

        binding.uploadTrackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"audio/*", "video/*"});
            startActivityForResult(intent, CreateTrackViewModel.PICK_FILE);
        });

        binding.cancelBtn.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });

        binding.addBtn.setOnClickListener(v -> {
            if (!binding.titleEt.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), R.string.uploading_track, Toast.LENGTH_LONG).show();
                createTrackViewModel.initCloudinaryManager(requireContext());

                createTrackViewModel.setTrackFileName(binding.titleEt.getText().toString());
                createTrackViewModel.uploadTrack(binding.genreSpinner.getSelectedItemPosition(), new CreateTrackCallback() {
                    @Override
                    public void onTrackCreated() {
                        NavHostFragment.findNavController(CreateTrackFragment.this).popBackStack();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Toast.makeText(getContext(), R.string.error_uploading_track, Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(getContext(), R.string.error_enter_name_track, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void subscribeObservers() {
        createTrackViewModel.getGenres().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null && genres.size() > 0) {
                ArrayList<String> spinnerItems = new ArrayList<>();
                for (Genre g : genres) {
                    spinnerItems.add(g.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.genreSpinner.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case CreateTrackViewModel.PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data.getData() != null) {
                            createTrackViewModel.setThumbnailUri(data.getData());
                            String path = data.getData().getPath();
                            if (path != null) {
                                final String filename = path.substring(path.lastIndexOf("/") + 1);
                                final InputStream imageStream = requireActivity().getContentResolver().openInputStream(data.getData());
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                binding.thumbnailIv.setImageBitmap(selectedImage);
                                createTrackViewModel.setThumbnailFileName(filename);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case CreateTrackViewModel.PICK_FILE:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        createTrackViewModel.setTrackUri(data.getData());
                        String path = data.getData().getPath();
                        if (path != null) {
                            final String filename = path.substring(path.lastIndexOf("/") + 1);
                            binding.trackFileTv.setText(filename);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), R.string.pick_a_document, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }
}