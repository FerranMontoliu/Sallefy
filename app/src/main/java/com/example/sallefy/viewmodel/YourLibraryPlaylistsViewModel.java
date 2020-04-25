package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class YourLibraryPlaylistsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    @Inject
    public YourLibraryPlaylistsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }
}
