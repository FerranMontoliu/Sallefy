package com.example.sallefy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sallefy.auth.Session;
import com.example.sallefy.network.SallefyRepository;
import com.example.sallefy.network.callback.DeleteUserCallback;

import javax.inject.Inject;

public class OptionsViewModel extends ViewModel {

    private SallefyRepository sallefyRepository;
    private boolean mDeleted;

    @Inject
    public OptionsViewModel(SallefyRepository sallefyRepository) {
        this.sallefyRepository = sallefyRepository;
        this.mDeleted = false;
    }

    public void logOut() {
        Session.setUser(null);
    }

    private void deleteAttempt() {
        sallefyRepository.deleteUser(new DeleteUserCallback() {
            @Override
            public void onAccountDeleted() {
                mDeleted = true;
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public boolean deleteUser() {
        deleteAttempt();
        return mDeleted;
    }
}
