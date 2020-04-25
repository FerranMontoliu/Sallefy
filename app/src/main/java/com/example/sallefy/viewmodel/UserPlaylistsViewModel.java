package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class UserPlaylistsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    @Inject
    public UserPlaylistsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }
}
