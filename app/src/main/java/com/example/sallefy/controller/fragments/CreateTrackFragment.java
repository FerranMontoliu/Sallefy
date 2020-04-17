package com.example.sallefy.controller.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sallefy.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CreateTrackFragment extends Fragment {

    public static final String TAG = CreateTrackFragment.class.getName();
    private static final int PICK_IMAGE = 0;
    private static final int PICK_FILE = 1;

    private ImageView ivThumbnail;
    private TextView tvFileName;

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

        ivThumbnail = v.findViewById(R.id.thumbnail_iv);
        tvFileName = v.findViewById(R.id.track_file_tv);

        v.findViewById(R.id.upload_thumbnail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        v.findViewById(R.id.upload_track_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_FILE);
            }
        });

        v.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getParentFragment() != null;
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
            }
        });

        v.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ACCEPT
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = data.getData();
                        assert imageUri != null;
                        final InputStream imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ivThumbnail.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), R.string.pick_an_image, Toast.LENGTH_LONG).show();
                }
                break;

            case PICK_FILE:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri documentUri = data.getData();
                        assert documentUri != null;
                        final InputStream documentStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(documentUri);
                        String path = documentUri.getPath();
                        assert path != null;
                        String filename = path.substring(path.lastIndexOf("/") + 1);
                        tvFileName.setText(filename);
                        // TODO: finish...

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), R.string.pick_a_document, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}