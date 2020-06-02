package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class StatisticsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;

    @Inject
    public StatisticsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }

}
