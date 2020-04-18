package com.example.sallefy.controller.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sallefy.R;
import com.example.sallefy.controller.restapi.callback.GenreCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.controller.restapi.manager.CloudinaryManager;
import com.example.sallefy.controller.restapi.manager.GenreManager;
import com.example.sallefy.controller.restapi.manager.TrackManager;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.Track;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class CreateTrackFragment extends Fragment implements GenreCallback, TrackCallback {

    public static final String TAG = CreateTrackFragment.class.getName();
    private static final int PICK_IMAGE = 0;
    private static final int PICK_FILE = 1;

    private ImageView ivThumbnail;
    private TextView tvFileName;
    private Spinner genreSpinner;

    private List<Genre> genres;

    private Genre genre;
    private Uri audioFileUri;
    private String audioFileName;
    private Uri thumbnailUri;
    private String thumbnailName;

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

        GenreManager.getInstance(getContext()).getAllGenres(this);

        ivThumbnail = v.findViewById(R.id.thumbnail_iv);
        tvFileName = v.findViewById(R.id.track_file_tv);
        genreSpinner = v.findViewById(R.id.genre_spinner);

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

                CloudinaryManager.getInstance(getContext(), CreateTrackFragment.this).uploadThumbnailFile(thumbnailUri, thumbnailName);
                CloudinaryManager.getInstance(getContext(), CreateTrackFragment.this).uploadAudioFile(audioFileUri, audioFileName,
                        genres.get(genreSpinner.getSelectedItemPosition()));

                assert getParentFragment() != null;
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
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
                        thumbnailUri = data.getData();

                        String path = audioFileUri.getPath();
                        assert path != null;
                        thumbnailName = path.substring(path.lastIndexOf("/") + 1);

                        assert thumbnailUri != null;
                        final InputStream imageStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(thumbnailUri);
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
                    audioFileUri = data.getData();
                    assert audioFileUri != null;
                    String path = audioFileUri.getPath();
                    assert path != null;
                    audioFileName = path.substring(path.lastIndexOf("/") + 1);
                    tvFileName.setText(audioFileName);

                } else {
                    Toast.makeText(getContext(), R.string.pick_a_document, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onGenresReceived(List<Genre> genres) {
        this.genres = genres;

        ArrayList<String> spinnerItems = new ArrayList<>();
        for (Genre g : genres) {
            spinnerItems.add(g.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, spinnerItems);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(adapter);
    }

    @Override
    public void onNoGenres(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_getting_genres, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(Throwable throwable) {
        Toast.makeText(getContext(), R.string.exploded, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTracksReceived(List<Track> tracks) {
        // UNUSED
    }

    @Override
    public void onNoTracks(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_uploading_track, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateTrack() {
        Toast.makeText(getContext(), R.string.track_uploaded, Toast.LENGTH_LONG).show();
    }
}