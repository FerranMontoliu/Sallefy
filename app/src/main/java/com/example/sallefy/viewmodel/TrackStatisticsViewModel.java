package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class TrackStatisticsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;

    @Inject
    public TrackStatisticsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.track = null;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }
}
