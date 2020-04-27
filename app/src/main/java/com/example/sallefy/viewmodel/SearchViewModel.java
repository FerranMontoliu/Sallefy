package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    @Inject
    public SearchViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }
}