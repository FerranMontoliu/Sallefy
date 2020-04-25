package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class YourLibraryViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    @Inject
    public YourLibraryViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }
}
