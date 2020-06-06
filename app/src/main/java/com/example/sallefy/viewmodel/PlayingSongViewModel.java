package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.model.Track;
import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class PlayingSongViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private Track track;

    @Inject
    public PlayingSongViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
