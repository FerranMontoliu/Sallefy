package com.example.sallefy.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.model.Track_;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.objectbox.ObjectBox;

import javax.inject.Inject;

import io.objectbox.BoxStore;

public class TrackOptionsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;
    private MutableLiveData<Boolean> mIsDownloaded;
    private BoxStore boxStore;

    @Inject
    public TrackOptionsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.track = null;
        this.mIsDownloaded = new MutableLiveData<>();
        boxStore = ObjectBox.getBoxStore();
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public LiveData<Boolean> isDownloaded(){
        requestIsDownloaded();
        return mIsDownloaded;
    }

    private void requestIsDownloaded(){
        if (boxStore.boxFor(Track.class).query().equal(Track_.id, track.getId()).build().count() != 0){
            mIsDownloaded.postValue(true);
        } else {
            mIsDownloaded.postValue(false);
        }
    }

    public void downloadTrackToggle(){
        if (!mIsDownloaded.getValue()) {
            boxStore.boxFor(Track.class).put(track);
            //TODO: Download track URL
        } else {
            boxStore.boxFor(Track.class).remove(track.getId());
        }
        mIsDownloaded.postValue(!mIsDownloaded.getValue());
    }
}
