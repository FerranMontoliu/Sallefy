package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.auth.Session;
import com.example.sallefy.model.User;
import com.example.sallefy.network.SallefyRepository;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private User user;

    @Inject
    public ProfileViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.user = null;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserImage() {
        return user.getImageUrl();
    }

    public String getUserName() {
        return user.getLogin();
    }

    public boolean isOwnUser() {
        if (this.user != null)
            return Session.getUser() == this.user;

        return false;
    }
}
