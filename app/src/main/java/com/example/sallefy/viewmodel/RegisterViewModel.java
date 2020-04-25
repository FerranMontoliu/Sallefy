package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.RegisterCallback;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {
    private SallefyRepository sallefyRepository;

    @Inject
    public RegisterViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
    }

    public void register(String email, String username, String password, RegisterCallback callback) {
        sallefyRepository.registerAttempt(email, username, password, callback);
    }
}
