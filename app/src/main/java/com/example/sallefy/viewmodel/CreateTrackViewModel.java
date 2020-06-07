package com.example.sallefy.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.sallefy.model.Genre;
import com.example.sallefy.model.NewTrack;
import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.CreateTrackCallback;
import com.example.sallefy.network.callback.GenreCallback;
import com.example.sallefy.utils.CloudinaryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class CreateTrackViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    public static final int PICK_IMAGE = 0;
    public static final int PICK_FILE = 1;
    public static final int PICK_VIDEO = 2;

    private MutableLiveData<List<Genre>> mGenres;
    private String trackFileName;
    private Uri trackUri;
    private String thumbnailFileName;
    private Uri thumbnailUri;
    private String thumbnailUrl;
    private String videoFileName;
    private Uri videoUri;


    @Inject
    public CreateTrackViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mGenres = new MutableLiveData<>();
    }

    private void requestGenres() {
        sallefyRepository.getAllGenres(new GenreCallback() {
            @Override
            public void onGenresReceived(List<Genre> genres) {
                mGenres.postValue(genres);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void uploadTrack(int genrePosition, CreateTrackCallback callback) {
        if (videoUri != null && thumbnailUri == null) {
            uploadVideoFile(genrePosition, callback);

        } else if (videoUri != null) {
            uploadVideoAndThumbnailFile(genrePosition, callback);

        } else if (trackUri != null && thumbnailUri == null) {
            uploadTrackFile(genrePosition, callback);

        } else if (trackUri != null) {
            uploadTrackAndThumbnailFile(genrePosition, callback);
        }
    }

    public LiveData<List<Genre>> getGenres() {
        requestGenres();
        return mGenres;
    }

    public void setTrackFileName(String filename) {
        trackFileName = filename;
    }

    public void setThumbnailFileName(String filename) {
        thumbnailFileName = filename;
    }

    public void setTrackUri(Uri uri) {
        trackUri = uri;
    }

    public void setThumbnailUri(Uri uri) {
        thumbnailUri = uri;
    }

    public void initCloudinaryManager(Context context) {
        CloudinaryManager.getInstance(context);
    }

    private void uploadTrackFile(int genrePosition, CreateTrackCallback callback) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", trackFileName);
        options.put("folder", "sallefy/songs");
        options.put("resource_type", "video");

        MediaManager.get().upload(trackUri)
                .unsigned(trackFileName)
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
                        NewTrack track = new NewTrack();
                        track.setName(trackFileName);
                        if (thumbnailUrl != null)
                            track.setThumbnail(thumbnailUrl);



                        track.setUrl((String) resultData.get("url"));
                        ArrayList<Genre> genres = new ArrayList<>();
                        genres.add(mGenres.getValue().get(genrePosition));
                        track.setGenres(genres);

                        sallefyRepository.createTrack(track, callback);
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

    private void uploadTrackAndThumbnailFile(int genrePosition, CreateTrackCallback callback) {
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
                        thumbnailUrl = (String) resultData.get("url");
                        uploadTrackFile(genrePosition, callback);
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


    private void uploadVideoFile(int genrePosition, CreateTrackCallback callback) {
        Map<String, Object> options = new HashMap<>();
        options.put("public_id", videoFileName);
        options.put("folder", "sallefy/songs");
        options.put("resource_type", "video");

        MediaManager.get().upload(videoUri)
                .unsigned(videoFileName)
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
                        NewTrack track = new NewTrack();
                        track.setName(trackFileName);
                        if (thumbnailUrl != null)
                            track.setThumbnail(thumbnailUrl);

                        track.setUrl((String) resultData.get("url"));
                        ArrayList<Genre> genres = new ArrayList<>();
                        genres.add(mGenres.getValue().get(genrePosition));
                        track.setGenres(genres);

                        sallefyRepository.createTrack(track, callback);
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

    private void uploadVideoAndThumbnailFile(int genrePosition, CreateTrackCallback callback) {
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
                        thumbnailUrl = (String) resultData.get("url");
                        uploadVideoFile(genrePosition, callback);
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

    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    public void setVideoUri(Uri videoUri) {
        this.videoUri = videoUri;
    }
}