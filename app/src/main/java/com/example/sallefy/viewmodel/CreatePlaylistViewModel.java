package com.example.sallefy.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.sallefy.model.Playlist;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.CreatePlaylistCallback;
import com.example.sallefy.utils.CloudinaryManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class CreatePlaylistViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    public static final int PICK_IMAGE = 0;

    private String thumbnailFileName;
    private Uri thumbnailUri;

    private Playlist mPlaylist;


    @Inject
    public CreatePlaylistViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mPlaylist = new Playlist();
    }

    public void uploadPlaylist(CreatePlaylistCallback callback) {
        if (thumbnailUri != null)
            createPlaylistAndThumbnail(callback);
        else
            createPlaylist(callback);
    }

    public void setPlaylistFileName(String playlistName) {
        mPlaylist.setName(playlistName);
    }

    public void setThumbnailFileName(String filename) {
        thumbnailFileName = filename;
    }


    public void setThumbnailUri(Uri uri) {
        thumbnailUri = uri;
    }

    public void initCloudinaryManager(Context context) {
        CloudinaryManager.getInstance(context);
    }

    private void createPlaylist(CreatePlaylistCallback callback) {
        sallefyRepository.createPlaylist(mPlaylist, callback);
    }

    private void createPlaylistAndThumbnail(CreatePlaylistCallback callback) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", thumbnailFileName);
        options.put("folder", "sallefy/thumbnails");
        options.put("resource_type", "image");

        MediaManager.get().upload(thumbnailUri)
                .unsigned(thumbnailFileName)
                .options(options)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        mPlaylist.setThumbnail((String) resultData.get("url"));
                        createPlaylist(callback);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .dispatch();
    }
}