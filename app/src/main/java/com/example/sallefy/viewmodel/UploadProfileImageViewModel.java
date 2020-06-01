package com.example.sallefy.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.UpdateUserCallback;
import com.example.sallefy.utils.CloudinaryManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UploadProfileImageViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    public static final int PICK_IMAGE = 0;

    private String photoFileName;
    private Uri photoUri;

    private User user;

    @Inject
    public UploadProfileImageViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.user = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void uploadPhoto(UpdateUserCallback callback) {
        if (photoUri != null)
            uploadPhotoCloud(callback);
    }

    public void setPhotoFileName(String filename) {
        this.photoFileName = filename;
    }

    public void setPhotoUri(Uri uri) {
        this.photoUri = uri;
    }

    public void initCloudinaryManager(Context context) {
        CloudinaryManager.getInstance(context);
    }

    private void uploadPhotoCloud(UpdateUserCallback callback) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", photoFileName);
        options.put("folder", "sallefy/profile_images");
        options.put("resource_type", "image");

        MediaManager.get().upload(photoUri)
                .unsigned(photoFileName)
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
                        user.setImageUrl((String) resultData.get("url"));
                        sallefyRepository.updateUser(user, callback);
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