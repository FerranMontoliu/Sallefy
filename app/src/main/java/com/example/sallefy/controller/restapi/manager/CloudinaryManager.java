package com.example.sallefy.controller.restapi.manager;

import android.content.Context;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.sallefy.controller.restapi.callback.TrackCallback;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.Track;
import com.example.sallefy.utils.CloudinaryConfigs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CloudinaryManager extends AppCompatActivity {

    private static CloudinaryManager sManager;
    private Context mContext;
    private String mFileName;
    private String mThumbnailUrl;
    private Genre mGenre;
    private TrackCallback mCallback;

    public static CloudinaryManager getInstance(Context context, TrackCallback callback) {
        if (sManager == null) {
            sManager = new CloudinaryManager(context, callback);
        }
        return sManager;
    }

    public CloudinaryManager(Context context, TrackCallback callback) {
        mContext = context;
        mCallback = callback;
        MediaManager.init(mContext, CloudinaryConfigs.getConfigurations());
    }

    public synchronized void uploadAudioFile(Uri fileUri, String fileName, Genre genre) {
        mGenre = genre;
        mFileName = fileName;
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/songs/mobile");
        options.put("resource_type", "video");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new UploadAudioCallback())
                .dispatch();
    }

    public synchronized void uploadThumbnailFile(Uri fileUri, String fileName) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", fileName);
        options.put("folder", "sallefy/thumbnails/mobile");
        options.put("resource_type", "image");

        MediaManager.get().upload(fileUri)
                .unsigned(fileName)
                .options(options)
                .callback(new UploadThumbnailCallback())
                .dispatch();
    }

    private class UploadThumbnailCallback implements UploadCallback {

        @Override
        public void onStart(String requestId) {
        }
        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            Double progress = (double) bytes/totalBytes;
        }
        @Override
        public void onSuccess(String requestId, Map resultData) {
            mThumbnailUrl = (String) resultData.get("url");
        }
        @Override
        public void onError(String requestId, ErrorInfo error) {
        }
        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
        }
    }

    private class UploadAudioCallback implements UploadCallback {

        @Override
        public void onStart(String requestId) {
        }
        @Override
        public void onProgress(String requestId, long bytes, long totalBytes) {
            Double progress = (double) bytes/totalBytes;
        }
        @Override
        public void onSuccess(String requestId, Map resultData) {
            Track track = new Track();
            track.setId(null);
            track.setName(mFileName);
            track.setThumbnail(mThumbnailUrl);
            track.setUrl((String) resultData.get("url"));
            ArrayList<Genre> genres = new ArrayList<>();
            genres.add(mGenre);
            track.setGenres(genres);
            TrackManager.getInstance(mContext).createTrack(track, mCallback);
        }
        @Override
        public void onError(String requestId, ErrorInfo error) {
        }
        @Override
        public void onReschedule(String requestId, ErrorInfo error) {
        }
    }
}
