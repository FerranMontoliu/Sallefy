package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class TrackOptionsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;

    @Inject
    public TrackOptionsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.track = null;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
