package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.model.Track_;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.LikeTrackCallback;
import com.example.sallefy.objectbox.ObjectBox;

import javax.inject.Inject;

import io.objectbox.BoxStore;

public class TrackOptionsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;
    private MutableLiveData<Boolean> mIsDownloaded;
    private MutableLiveData<Boolean> mIsLiked;
    private BoxStore boxStore;

    @Inject
    public TrackOptionsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.track = null;
        this.mIsDownloaded = new MutableLiveData<>();
        this.mIsLiked = new MutableLiveData<>();
        this.boxStore = ObjectBox.getBoxStore();
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public LiveData<Boolean> isDownloaded() {
        requestIsDownloaded();
        return mIsDownloaded;
    }

    public LiveData<Boolean> isLiked(){
        requestIsLiked();
        return mIsLiked;
    }

    private void requestIsLiked() {
        mIsLiked.postValue(track.isLiked());
    }

    private void requestIsDownloaded() {
        mIsDownloaded.postValue(boxStore.boxFor(Track.class).query().equal(Track_.id, track.getId()).build().count() != 0);
    }

    public void downloadTrackToggle() {
        if (!mIsDownloaded.getValue()) {
            boxStore.boxFor(Track.class).put(track);
            //TODO: Download track URL
        } else {
            boxStore.boxFor(Track.class).remove(track.getId());
        }
        mIsDownloaded.postValue(!mIsDownloaded.getValue());
    }

    public void likeTrackToggle() {
        sallefyRepository.likeTrack(track, new LikeTrackCallback() {
            @Override
            public void onTrackLiked() {
                track.setLiked(!mIsLiked.getValue());
                mIsLiked.postValue(track.isLiked());
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }
}
